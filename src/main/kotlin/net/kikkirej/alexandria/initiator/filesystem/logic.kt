package net.kikkirej.alexandria.initiator.filesystem

import net.kikkirej.alexandria.initiator.filesystem.camunda.CamundaLayer
import net.kikkirej.alexandria.initiator.filesystem.config.FileSystemInitConfig
import net.kikkirej.alexandria.initiator.filesystem.config.FilesystemSourceConfig
import net.kikkirej.alexandria.initiator.filesystem.config.GeneralProperties
import net.kikkirej.alexandria.initiator.filesystem.exception.WrongSourceException
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Date
import java.sql.Timestamp

private const val cron = "";

@Service
class FilesystemInitiatorScheduler(@Autowired val config: FileSystemInitConfig,
                                   @Autowired val sourceRepository: SourceRepository,
                                   @Autowired val projectRepository: ProjectRepository,
                                   @Autowired val versionRepository: VersionRepository,
                                   @Autowired val analysisRepository: AnalysisRepository,
                                   @Autowired val copyUtil: CopyUtil,
                                   @Autowired val camundaLayer: CamundaLayer){

    val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelay = 100, fixedRate = 24*60*60*1000)
    //@Scheduled(cron = "${alexandria.initiator.filesystem.cron}")
    fun scheduled(){
        log.info("starting analysis")
        for (sourceConfig in config.sources) {
            log.info("analyzing source: " + sourceConfig)
            val source: Source
            try {
                source = getSourceObject(sourceConfig)
            }catch (exception:WrongSourceException){
                continue;
            }
            handleSource(source, sourceConfig.path)
        }
    }

    private fun getSourceObject(source: FilesystemSourceConfig): Source {
        val sourceOptional = sourceRepository.findById(source.id);
        if (sourceOptional.isPresent()){
            val sourceObject = sourceOptional.get()
            if(sourceObject.type.equals("Filesystem") == false){
                throw WrongSourceException("Source with id " + source.id + " has type '" + sourceObject.type + "' and not 'Filesystem'")
            }
            return sourceObject;
        }else{
            val sourceObject = Source(id = source.id, name = source.name)
            sourceRepository.save(sourceObject)
            return sourceObject;
        }
    }

    private fun handleSource(source: Source, path: String) {
        val searchDirectory = File(path)
        val isDirectory = searchDirectory.isDirectory
        if(!isDirectory){
            return;
        }
        val files = searchDirectory.listFiles()
        for (file in files){
            handleFile(file, source)
        }
    }

    private fun handleFile(file: File?, source: Source) {
        if(file == null || file.isDirectory.equals(false)){
            return;
        }
        val project = getProject(source, file)
        val version = getVersion(project)
        val analysis = createAnalysis(version)
        copyFolderContents(file, analysis.id)
        camundaLayer.startProcess(project, version, analysis)
    }

    private fun copyFolderContents(file: File, id: Long) {
        copyUtil.copy(file, id)
    }

    private fun createAnalysis(version: Version): Analysis {
        val analysis = Analysis(id = 0, version = version, creationTime = Timestamp(Date().time))
        analysisRepository.save(analysis)
        return analysis;
    }

    private fun getVersion(project: Project): Version {
        val versionOptional = versionRepository.findByProject(project)
        if(versionOptional.isPresent){
            return versionOptional.get()
        }else{
            val version = Version(id = 0, default_version = true, name = "folder", project = project, metadata = setOf())
            versionRepository.save(version)
            return version
        }
    }

    private fun getProject(source: Source, file: File) : Project {
        val projectOptional = projectRepository.findBySourceAndExternalIdentifier(source, file.absolutePath)
        if(projectOptional.isPresent) {
            return projectOptional.get();
        }else{
            val project =
                Project(externalIdentifier = file.absolutePath, metadata = setOf(), source = source, id = 0, url = file.name)
            projectRepository.save(project);
            return project;
        }
    }
}

@Service
class CopyUtil(@Autowired val generalProperties: GeneralProperties){
    fun copy(source: File, id: Long) {
        val destinationPath :String = generalProperties.sharedfolder + File.separator + id;
        val destination = File(destinationPath)
        //destination.mkdirs()
        FileUtils.copyDirectory(source, destination)
    }

}