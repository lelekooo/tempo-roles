package com.tempo.assessment.rolesapp.configuration

import com.tempo.assessment.rolesapp.observability.LoggingHandler
import feign.Logger
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ObservabilityConfiguration {
    @Bean
    fun observedAspect(observationRegistry: ObservationRegistry): ObservedAspect {
        observationRegistry.observationConfig().observationHandler(LoggingHandler())
        return ObservedAspect(observationRegistry)
    }
}