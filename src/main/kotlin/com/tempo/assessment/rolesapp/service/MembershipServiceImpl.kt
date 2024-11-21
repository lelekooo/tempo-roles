package com.tempo.assessment.rolesapp.service

import com.tempo.assessment.rolesapp.exception.IllegalAssignmentException
import com.tempo.assessment.rolesapp.exception.RequiredResourceNotFoundException
import com.tempo.assessment.rolesapp.exception.ResourceAlreadyExistsException
import com.tempo.assessment.rolesapp.model.MembershipDTO
import com.tempo.assessment.rolesapp.model.MembershipFilterDTO
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.model.MembershipPatchDTO
import com.tempo.assessment.rolesapp.model.extensions.toDTO
import com.tempo.assessment.rolesapp.model.extensions.toEntity
import com.tempo.assessment.rolesapp.repository.MembershipRepository
import com.tempo.assessment.rolesapp.repository.RoleRepository
import com.tempo.assessment.rolesapp.repository.entity.Membership
import com.tempo.assessment.rolesapp.repository.external.TeamRepository
import com.tempo.assessment.rolesapp.repository.external.UserRepository
import com.tempo.assessment.rolesapp.repository.external.transferobject.TeamTO
import io.micrometer.observation.annotation.Observed
import io.micrometer.tracing.Tracer
import jakarta.validation.Valid
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
@Observed(name = "membershipService")
class MembershipServiceImpl(
    private val membershipRepository: MembershipRepository,
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val tracer: Tracer
) : MembershipService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Value("\${tempo.defaultRoleId}")
    lateinit var defaultRoleId: String

    override fun assignRoleToMember(membershipInputDTO: MembershipInputDTO): MembershipDTO {
        log.info("Creating membership for $membershipInputDTO")
        validateMembershipInput(membershipInputDTO)
        val existingMembership = membershipRepository.findByTeamIdAndMemberId(
            membershipInputDTO.teamId,
            membershipInputDTO.memberId
        )
        return if (existingMembership != null) {
            throw ResourceAlreadyExistsException("Membership already exists.")
        } else {
            val roleId = membershipInputDTO.roleId ?: defaultRoleId
            val savedMembership = membershipRepository.save(
                Membership(
                    teamId = membershipInputDTO.teamId,
                    roleId = roleId,
                    memberId = membershipInputDTO.memberId
                )
            ).toDTO()
            log.info("Successfully created membership for $membershipInputDTO")
            savedMembership
        }
    }

    private fun validateMembershipInput(membershipInputDTO: MembershipInputDTO) {
        runBlocking {
            val span = tracer.currentSpan()
            val (team, member, role) = withContext(Dispatchers.IO) {
                awaitAll(
                    async {
                        tracer.withSpan(span)
                        teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId)
                    },
                    async {
                        tracer.withSpan(span)
                        userRepository.getUserByIdOrNull(membershipInputDTO.memberId)
                    },
                    async {
                        tracer.withSpan(span)
                        roleRepository.findByIdOrNull(membershipInputDTO.roleId ?: defaultRoleId)
                    }
                )
            }

            requireNotNull(team) {
                val errorMessage = "Team with ID '${membershipInputDTO.teamId}' not found."
                throw RequiredResourceNotFoundException(errorMessage)
            }
            requireNotNull(member) {
                val errorMessage = "Member with ID '${membershipInputDTO.memberId}' not found."
                throw RequiredResourceNotFoundException(errorMessage)
            }
            requireNotNull(role) {
                val errorMessage = "Role with ID '${membershipInputDTO.roleId}' not found."
                throw RequiredResourceNotFoundException(errorMessage)
            }

            val teamTO = team as TeamTO
            val teamMemberIds = teamTO.teamMemberIds
            val isMemberOrLead =
                membershipInputDTO.memberId == teamTO.teamLeadId || membershipInputDTO.memberId in teamMemberIds
            require(isMemberOrLead) { throw IllegalAssignmentException("Member '${membershipInputDTO.memberId}' is not part of the team.") }
        }
    }

    override fun updateMembership(membershipPatchDTO: MembershipPatchDTO): MembershipDTO {
        val membership = membershipRepository.findByIdOrNull(membershipPatchDTO.id)
            ?: throw RequiredResourceNotFoundException("Membership with ID '${membershipPatchDTO.id}' not found.")
        membershipPatchDTO.teamId?.let { membership.teamId = it }
        membershipPatchDTO.memberId?.let { membership.memberId = it }
        membershipPatchDTO.roleId?.let { membership.roleId = it }
        val updatedMembership = membershipRepository.save(membership)
        return updatedMembership.toDTO()
    }

    override fun replaceMembership(membershipDTO: MembershipDTO): MembershipDTO {
        membershipRepository.findByIdOrNull(membershipDTO.id)
            ?: throw RequiredResourceNotFoundException("Membership with ID '${membershipDTO.id}' not found.")
        val updatedMembership = membershipRepository.save(membershipDTO.toEntity())
        return updatedMembership.toDTO()
    }

    override fun deleteMembership(id: String) {
        val membership = membershipRepository.findByIdOrNull(id)
            ?: throw RequiredResourceNotFoundException("Membership with ID '$id' not found.")
        membershipRepository.delete(membership)
    }

    override fun filterMemberships(membershipFilterDTO: MembershipFilterDTO): List<MembershipDTO> {
        log.info("Filtering memberships for $membershipFilterDTO")
        val specification = Specification.where(equal("teamId", membershipFilterDTO.teamId))
            .and(equal("memberId", membershipFilterDTO.memberId))
            .and(equal("roleId", membershipFilterDTO.roleId))
            .and(equal("id", membershipFilterDTO.id))

        val memberships = membershipRepository.findAll(specification)
        return memberships.map { it.toDTO() }
    }

    private fun equal(attribute: String, value: Any?): Specification<Membership> {
        return Specification { root, _, criteriaBuilder ->
            value?.let {
                criteriaBuilder.equal(root.get<String>(attribute), value)
            }
        }
    }
}