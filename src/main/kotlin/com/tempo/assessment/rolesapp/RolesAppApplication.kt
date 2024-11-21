package com.tempo.assessment.rolesapp

import com.tempo.assessment.rolesapp.configuration.JPAConfiguration
import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import

@EnableFeignClients
@EnableAdminServer
@SpringBootApplication
@EnableCaching
@Import(JPAConfiguration::class)
class RolesAppApplication

fun main(args: Array<String>) {
	runApplication<RolesAppApplication>(*args)
}
