package com.tempo.assessment.rolesapp.repository.external.client

import com.tempo.assessment.rolesapp.repository.external.transferobject.UserTO
import io.micrometer.observation.annotation.Observed
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Observed(name = "userRepositoryClient")
@FeignClient(name = "user-api", url = "\${external-api.user.url}")
interface UserApiClient {

    @GetMapping("/{userId}")
    @Cacheable(cacheNames = ["users"], key = "#userId")
    fun getUserById(@PathVariable("userId") userId: String): UserTO
}