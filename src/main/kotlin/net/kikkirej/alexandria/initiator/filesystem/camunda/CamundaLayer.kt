package net.kikkirej.alexandria.initiator.filesystem.camunda

import net.kikkirej.alexandria.initiator.filesystem.Analysis
import net.kikkirej.alexandria.initiator.filesystem.Project
import net.kikkirej.alexandria.initiator.filesystem.Version

interface CamundaLayer {
    fun startProcess(analysis_id: Project, version: Version, analysis: Analysis)
}