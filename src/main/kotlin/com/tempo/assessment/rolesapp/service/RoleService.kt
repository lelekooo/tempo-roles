package com.tempo.assessment.rolesapp.service

import com.tempo.assessment.rolesapp.model.RoleDTO
import com.tempo.assessment.rolesapp.model.RoleInputDTO
import com.tempo.assessment.rolesapp.service.validation.ValidUUID
import jakarta.validation.Valid

interface RoleService {

    fun getRoles(): List<RoleDTO>
    fun getRoleById(id: String): RoleDTO?
    fun createRole(@Valid roleInputDTO: RoleInputDTO): RoleDTO
}
