package net.kikkirej.alexandria.initiator.filesystem

import org.springframework.data.repository.CrudRepository
import java.util.*

interface SourceRepository : CrudRepository<Source, Long>

interface ProjectRepository : CrudRepository<Project, Long>{
    fun findBySourceAndExternalIdentifier(source: Source, identifier: String): Optional<Project>
}

interface VersionRepository : CrudRepository<Version, Long> {
    fun findByProject(project: Project) : Optional<Version>
}

interface AnalysisRepository : CrudRepository<Analysis, Long>