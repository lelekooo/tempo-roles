package com.tempo.assessment.rolesapp.controller

import com.tempo.assessment.rolesapp.model.MembershipDTO
import com.tempo.assessment.rolesapp.model.MembershipFilterDTO
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.model.MembershipPatchDTO
import com.tempo.assessment.rolesapp.service.MembershipService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/membership")
class MembershipController(private val membershipService: MembershipService) {

    @PostMapping
    fun createMembership(@RequestBody membershipInputDTO: MembershipInputDTO): ResponseEntity<MembershipDTO> {
        val membership = membershipService.assignRoleToMember(membershipInputDTO)
        return ResponseEntity<MembershipDTO>(membership, HttpStatus.CREATED)
    }

    @PatchMapping("/{id}")
    fun updateMembershipWithoutValidation(
        @PathVariable id: String,
        @RequestBody membershipPatchDTO: MembershipPatchDTO
    ): ResponseEntity<MembershipDTO> {
        val updatedMembership = membershipService.updateMembership(membershipPatchDTO)
        return ResponseEntity.ok(updatedMembership)
    }

    @PutMapping("/{id}")
    fun replaceMembershipWithoutValidation(
        @PathVariable id: String,
        @RequestBody membershipDTO: MembershipDTO
    ): ResponseEntity<MembershipDTO> {
        val replacedMembership = membershipService.replaceMembership(membershipDTO)
        return ResponseEntity.ok(replacedMembership)
    }

    @DeleteMapping("/{id}")
    fun deleteMembership(@PathVariable id: String): ResponseEntity<Unit>{
        membershipService.deleteMembership(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping
    fun filterMemberships(@ParameterObject membershipFilterDTO: MembershipFilterDTO): ResponseEntity<List<MembershipDTO>> {
        return ResponseEntity.ok(membershipService.filterMemberships(membershipFilterDTO))
    }
}