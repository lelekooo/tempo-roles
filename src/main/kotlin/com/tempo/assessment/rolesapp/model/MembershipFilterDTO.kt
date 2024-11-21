package com.tempo.assessment.rolesapp.model

import com.tempo.assessment.rolesapp.service.validation.ValidUUID
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.RequestParam

data class MembershipFilterDTO (
    val id: String?,
    val teamId: String?,
    val roleId: String?,
    val memberId: String?
)
