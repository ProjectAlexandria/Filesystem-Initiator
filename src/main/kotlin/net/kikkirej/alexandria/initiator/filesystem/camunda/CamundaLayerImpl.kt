package net.kikkirej.alexandria.initiator.filesystem.camunda

import net.kikkirej.alexandria.initiator.filesystem.Analysis
import net.kikkirej.alexandria.initiator.filesystem.Project
import net.kikkirej.alexandria.initiator.filesystem.Version
import net.kikkirej.alexandria.initiator.filesystem.config.GeneralProperties
import net.kikkirej.alexandria.initiator.filesystem.config.InitConfig
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.File

@Service
class CamundaLayerImpl (@Qualifier("remote") val runtimeService: RuntimeService,
                        @Autowired val initConfig: InitConfig,
                        @Autowired val generalProperties: GeneralProperties) : CamundaLayer{

    override fun startProcess(project: Project, version: Version, analysis: Analysis) {
        val processVariables = mapOf<String, String>("version_name" to version.name,
            "source_type" to project.source.type,
            "source_name" to project.source.name,
            "path" to getFilePath(analysis))
        val processInstance = runtimeService.startProcessInstanceByKey(initConfig.processKey, analysis.id.toString(), processVariables)
    }

    private fun getFilePath(analysis: Analysis): String {
        val upperFolder = File(generalProperties.sharedfolder);
        val analysisFolder = File(upperFolder.absolutePath + File.separator + analysis.id)
        return analysisFolder.absolutePath
    }

}