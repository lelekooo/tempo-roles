package com.tempo.assessment.rolesapp.model.extensions

import com.tempo.assessment.rolesapp.model.MembershipDTO
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.repository.entity.Membership

import com.tempo.assessment.rolesapp.model.RoleDTO
import com.tempo.assessment.rolesapp.repository.entity.Role

fun RoleDTO.toEntity(): Role {
    return Role(
        id = this.id,
        name = this.name
    )
}

fun Role.toDTO(): RoleDTO {
    return RoleDTO(
        id = this.id,
        name = this.name
    )
}

