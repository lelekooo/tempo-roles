package com.tempo.assessment.rolesapp.model

import com.tempo.assessment.rolesapp.service.validation.ValidUUID
import jakarta.validation.constraints.NotBlank

data class MembershipInputDTO (
    @field:ValidUUID(message = "Team ID must be a valid UUID")
    @field:NotBlank(message = "Team ID must not be blank")
    val teamId: String,

    @field:ValidUUID(message = "Role ID must be a valid UUID")
    val roleId: String?,

    @field:ValidUUID(message = "Member ID must be a valid UUID")
    @field:NotBlank(message = "Member ID must not be blank")
    val memberId: String

)
