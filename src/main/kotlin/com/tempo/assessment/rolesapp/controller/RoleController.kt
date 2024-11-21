package com.tempo.assessment.rolesapp.controller

import com.tempo.assessment.rolesapp.model.RoleDTO
import com.tempo.assessment.rolesapp.model.RoleInputDTO
import com.tempo.assessment.rolesapp.repository.entity.Role
import com.tempo.assessment.rolesapp.service.RoleServiceImpl
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/role")
class RoleController(private val roleServiceImpl: RoleServiceImpl) {

    @GetMapping("/")
    fun getRoles(): List<RoleDTO> {
        return roleServiceImpl.getRoles()
    }

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: String): RoleDTO? {
        return roleServiceImpl.getRoleById(id)
    }

    @PostMapping("/")
    fun createRole(@RequestBody roleInputDTO: RoleInputDTO): RoleDTO {
        return roleServiceImpl.createRole(roleInputDTO)
    }
}
