package com.tempo.assessment.rolesapp.repository.external

import com.tempo.assessment.rolesapp.repository.external.client.UserApiClient
import com.tempo.assessment.rolesapp.repository.external.transferobject.UserTO
import io.micrometer.observation.annotation.Observed
import org.springframework.stereotype.Repository

@Repository
@Observed(name = "userRepository")
class UserRepositoryImpl(private val userClient: UserApiClient): UserRepository {

    override fun getUserByIdOrNull(userId: String): UserTO? {
        return userClient.getUserById(userId)
    }
}