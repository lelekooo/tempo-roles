package com.tempo.assessment.rolesapp.service

import com.tempo.assessment.rolesapp.model.MembershipDTO
import com.tempo.assessment.rolesapp.model.MembershipFilterDTO
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.model.MembershipPatchDTO
import jakarta.validation.Valid

interface MembershipService {

    fun assignRoleToMember(@Valid membershipInputDTO: MembershipInputDTO): MembershipDTO
    fun filterMemberships(membershipFilterDTO: MembershipFilterDTO): List<MembershipDTO>
    fun updateMembership(membershipPatchDTO: MembershipPatchDTO): MembershipDTO
    fun replaceMembership(membershipDTO: MembershipDTO): MembershipDTO
    fun deleteMembership(id: String)
}
