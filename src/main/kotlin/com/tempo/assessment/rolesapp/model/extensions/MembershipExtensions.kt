package com.tempo.assessment.rolesapp.model.extensions

import com.tempo.assessment.rolesapp.model.MembershipDTO
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.repository.entity.Membership

fun Membership.toDTO(): MembershipDTO {
    return MembershipDTO(
        id = this.id!!,
        teamId = this.teamId,
        memberId = this.memberId,
        roleId = this.roleId
    )
}

fun MembershipDTO.toEntity(): Membership {
    return Membership(
        id = this.id,
        teamId = this.teamId,
        memberId = this.memberId,
        roleId = this.roleId
    )
}