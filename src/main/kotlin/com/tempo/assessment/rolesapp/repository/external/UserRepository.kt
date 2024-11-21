package com.tempo.assessment.rolesapp.repository.external

import com.tempo.assessment.rolesapp.repository.external.transferobject.UserTO

interface UserRepository {
    fun getUserByIdOrNull(userId: String): UserTO?
}