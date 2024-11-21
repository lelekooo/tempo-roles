package com.tempo.assessment.rolesapp.repository.external.transferobject

data class TeamTO(
    val id: String,
    val name: String,
    val teamLeadId: String,
    val teamMemberIds: List<String>
)