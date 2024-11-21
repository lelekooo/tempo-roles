package com.tempo.assessment.rolesapp.repository.external.transferobject

data class UserTO(
    val id: String,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val avatarUrl: String,
    val location: String
)