package com.tempo.assessment.rolesapp.configuration

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories("com.tempo.assessment.rolesapp.repository")
@EntityScan("com.tempo.assessment.rolesapp.repository.entity")
class JPAConfiguration {
}

