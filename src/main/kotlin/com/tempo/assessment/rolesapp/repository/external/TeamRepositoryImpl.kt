package com.tempo.assessment.rolesapp.repository.external

import com.tempo.assessment.rolesapp.repository.external.client.TeamApiClient
import com.tempo.assessment.rolesapp.repository.external.transferobject.TeamTO
import io.micrometer.observation.annotation.Observed
import org.springframework.stereotype.Repository

@Repository
@Observed(name = "teamRepository")
class TeamRepositoryImpl(private val teamClient: TeamApiClient) : TeamRepository {

    override fun getTeamByIdOrNull(teamId: String): TeamTO? {
        return teamClient.getTeamById(teamId)
    }
}