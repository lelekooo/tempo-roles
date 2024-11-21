package com.tempo.assessment.rolesapp.repository

import com.tempo.assessment.rolesapp.repository.entity.Membership
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface MembershipRepository : JpaRepository<Membership, String>, JpaSpecificationExecutor<Membership> {
    fun findByTeamIdAndMemberId(teamId: String, memberId: String): Membership?
    fun findByTeamId(teamId: String): List<Membership>
}