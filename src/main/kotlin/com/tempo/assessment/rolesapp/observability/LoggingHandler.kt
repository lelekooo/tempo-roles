package com.tempo.assessment.rolesapp.observability

import io.micrometer.observation.Observation

import io.micrometer.observation.ObservationHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class LoggingHandler : ObservationHandler<Observation.Context?> {
    override fun supportsContext(context: Observation.Context): Boolean {
        return true
    }
}