package com.tempo.assessment.rolesapp.model

import jakarta.validation.constraints.NotBlank

data class RoleInputDTO(@field:NotBlank val name: String)
