package com.tempo.assessment.rolesapp.repository.external.client

import com.tempo.assessment.rolesapp.repository.external.transferobject.TeamTO
import io.micrometer.observation.annotation.Observed
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Observed(name = "teamRepositoryClient")
@FeignClient(name = "team-api", url = "\${external-api.team.url}")
interface TeamApiClient {

    @GetMapping("/{teamId}")
    @Cacheable(cacheNames = ["teams"], key = "#teamId")
    fun getTeamById(@PathVariable("teamId") teamId: String): TeamTO
}