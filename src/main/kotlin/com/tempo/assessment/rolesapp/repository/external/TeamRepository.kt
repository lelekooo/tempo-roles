package com.tempo.assessment.rolesapp.repository.external

import com.tempo.assessment.rolesapp.repository.external.transferobject.TeamTO

interface TeamRepository {
    fun getTeamByIdOrNull(teamId: String): TeamTO?
}