package com.tempo.assessment.rolesapp.service

import com.tempo.assessment.rolesapp.model.RoleDTO
import com.tempo.assessment.rolesapp.model.RoleInputDTO
import com.tempo.assessment.rolesapp.model.extensions.toDTO
import com.tempo.assessment.rolesapp.model.extensions.toEntity
import com.tempo.assessment.rolesapp.repository.RoleRepository
import com.tempo.assessment.rolesapp.repository.entity.Role
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
@Observed(name = "roleService")
class RoleServiceImpl(private val roleRepository: RoleRepository) : RoleService {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun getRoles(): List<RoleDTO> {
        log.info("Listing roles")
        val roles = roleRepository.findAll()
        return roles.map { it.toDTO() }
    }

    override fun getRoleById(id: String): RoleDTO? {
        log.info("Getting role by id: $id")
        val role = roleRepository.findByIdOrNull(id)
        return role?.toDTO()
    }

    override fun createRole(roleInputDTO: RoleInputDTO): RoleDTO {
        log.info("Creating role ${roleInputDTO.name}")
        val role = Role(name = roleInputDTO.name)
        val savedRole = roleRepository.save(role)
        log.info("Created role ${role.name}")
        return savedRole.toDTO()
    }
}