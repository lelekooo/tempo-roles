package com.tempo.assessment.rolesapp.model

import com.tempo.assessment.rolesapp.service.validation.ValidUUID
import jakarta.validation.constraints.NotBlank

data class MembershipDTO(
    val id: String,
    val teamId: String,
    val roleId: String,
    val memberId: String
)
