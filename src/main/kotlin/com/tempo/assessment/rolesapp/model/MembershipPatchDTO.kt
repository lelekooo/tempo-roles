package com.tempo.assessment.rolesapp.model

data class MembershipPatchDTO(
    val id: String,
    val teamId: String?,
    val memberId: String?,
    val roleId: String?
)
