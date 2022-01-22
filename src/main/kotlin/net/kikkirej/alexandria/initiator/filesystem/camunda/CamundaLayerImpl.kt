package net.kikkirej.alexandria.initiator.filesystem.camunda

import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CamundaLayerImpl (@Qualifier("remote") val runtimeService: RuntimeService) : CamundaLayer{
    override fun startProcess(analysis_id: Long) {
        val processInstance = runtimeService.startProcessInstanceByKey("pizza", analysis_id.toString())

    }

}