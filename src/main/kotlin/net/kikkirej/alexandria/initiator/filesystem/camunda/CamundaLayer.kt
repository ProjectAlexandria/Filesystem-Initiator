package net.kikkirej.alexandria.initiator.filesystem.camunda

interface CamundaLayer {
    fun startProcess(analysis_id: Long)
}