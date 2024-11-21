package com.tempo.assessment.rolesapp.repository

import com.tempo.assessment.rolesapp.repository.entity.Role
import io.micrometer.observation.annotation.Observed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Observed(name = "roleRepository")
interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role?
}