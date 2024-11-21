package com.tempo.assessment.rolesapp.service

import com.tempo.assessment.rolesapp.exception.IllegalAssignmentException
import com.tempo.assessment.rolesapp.exception.RequiredResourceNotFoundException
import com.tempo.assessment.rolesapp.exception.ResourceAlreadyExistsException
import com.tempo.assessment.rolesapp.model.MembershipFilterDTO
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.model.extensions.toDTO
import com.tempo.assessment.rolesapp.repository.MembershipRepository
import com.tempo.assessment.rolesapp.repository.RoleRepository
import com.tempo.assessment.rolesapp.repository.entity.Membership
import com.tempo.assessment.rolesapp.repository.entity.Role
import com.tempo.assessment.rolesapp.repository.external.TeamRepository
import com.tempo.assessment.rolesapp.repository.external.UserRepository
import com.tempo.assessment.rolesapp.repository.external.transferobject.TeamTO
import com.tempo.assessment.rolesapp.repository.external.transferobject.UserTO
import io.micrometer.tracing.Tracer
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class MembershipServiceImplTest {

    private val membershipRepository: MembershipRepository = mockk()
    private val teamRepository: TeamRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val roleRepository: RoleRepository = mockk()
    private val tracer: Tracer = mockk(relaxed = true)
    @Value("\${tempo.defaultRoleId}") lateinit var defaultRoleId: String

    private val membershipService = MembershipServiceImpl(
        membershipRepository,
        teamRepository,
        userRepository,
        roleRepository,
        tracer
    )

    @BeforeEach
    fun setUp(){
        clearAllMocks()
    }

    @Test
    fun `should assign role to member when all inputs are valid`() {
        val membershipInputDTO = createMembershipInputDTO()
        val team = createTeam(membershipInputDTO.teamId, listOf(membershipInputDTO.memberId))
        val member = createUser(membershipInputDTO.memberId)
        val roleId = membershipInputDTO.roleId!!
        val role = createRole(roleId)
        val membership = createMembership(
            null,
            membershipInputDTO.teamId,
            membershipInputDTO.memberId,
            roleId
        )


        every { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) } returns team
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns member
        every { roleRepository.findByIdOrNull(membershipInputDTO.roleId) } returns role
        every { membershipRepository.findByTeamIdAndMemberId(membershipInputDTO.teamId, membershipInputDTO.memberId) } returns null
        val membershipId = randomUUIDString()
        every { membershipRepository.save(membership) } returns membership.copy(id = membershipId)

        val membershipDTO = membershipService.assignRoleToMember(membershipInputDTO)

        assertEquals(membershipId, membershipDTO.id)
    }

    @Test
    fun `should assign role to team lead when all inputs are valid`() {
        val membershipInputDTO = createMembershipInputDTO()
        val team = createTeam(membershipInputDTO.teamId, listOf(randomUUIDString())).copy(teamLeadId = membershipInputDTO.memberId)
        val member = createUser(randomUUIDString())
        val roleId = membershipInputDTO.roleId!!
        val role = createRole(roleId)
        val membership = createMembership(
            null,
            membershipInputDTO.teamId,
            membershipInputDTO.memberId,
            roleId
        )


        every { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) } returns team
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns member
        every { roleRepository.findByIdOrNull(membershipInputDTO.roleId) } returns role
        every { membershipRepository.findByTeamIdAndMemberId(membershipInputDTO.teamId, membershipInputDTO.memberId) } returns null
        val membershipId = randomUUIDString()
        every { membershipRepository.save(membership) } returns membership.copy(id = membershipId)

        val membershipDTO = membershipService.assignRoleToMember(membershipInputDTO)

        assertEquals(membershipId, membershipDTO.id)
    }

    @Test
    fun `should throw RequiredResourceNotFoundException when team is not found`() {
        val membershipInputDTO = createMembershipInputDTO()
        val member = createUser(membershipInputDTO.memberId)

        every { teamRepository.getTeamByIdOrNull(any()) } returns null
        every { roleRepository.findByIdOrNull(membershipInputDTO.roleId) } returns createRole("existing-role")
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns member

        assertThrows<RequiredResourceNotFoundException> {
            runBlocking { membershipService.assignRoleToMember(membershipInputDTO) }
        }

        verify { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) }
        verify(exactly = 0) { membershipRepository.findByTeamIdAndMemberId(any(), any()) }
        verify(exactly = 0) { membershipRepository.save(any()) }
    }

    @Test
    fun `should throw RequiredResourceNotFoundException when member is not found`() {
        val membershipInputDTO = createMembershipInputDTO()
        val team = createTeam(membershipInputDTO.teamId, emptyList())
        val roleId = membershipInputDTO.roleId!!
        val role = createRole(roleId)

        every { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) } returns team
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns null
        every { roleRepository.findByIdOrNull(membershipInputDTO.roleId) } returns role

        assertThrows<RequiredResourceNotFoundException> {
            runBlocking { membershipService.assignRoleToMember(membershipInputDTO) }
        }

        verify(exactly = 0) { membershipRepository.findByTeamIdAndMemberId(any(), any()) }
        verify(exactly = 0) { membershipRepository.save(any()) }
    }

    @Test
    fun `should throw RequiredResourceNotFoundException when role is not found`() {
        val membershipInputDTO = createMembershipInputDTO()
        val team = createTeam(membershipInputDTO.teamId, emptyList())
        val member = createUser(membershipInputDTO.memberId)

        every { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) } returns team
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns member
        every { roleRepository.findByIdOrNull(any()) } returns null

        assertThrows<RequiredResourceNotFoundException> {
            runBlocking { membershipService.assignRoleToMember(membershipInputDTO) }
        }

        verify(exactly = 0) { membershipRepository.findByTeamIdAndMemberId(any(), any()) }
        verify(exactly = 0) { membershipRepository.save(any()) }
    }

    @Test
    fun `should throw ResourceAlreadyExistsException when membership already exists`() {
        val membershipInputDTO = createMembershipInputDTO()
        val team = createTeam(membershipInputDTO.teamId, listOf(membershipInputDTO.memberId))
        val member = createUser(membershipInputDTO.memberId)
        val roleId = membershipInputDTO.roleId!!
        val role = createRole(roleId)
        val existingMembership = createMembership(
            null,
            membershipInputDTO.teamId,
            membershipInputDTO.memberId,
            roleId
        )

        every { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) } returns team
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns member
        every { roleRepository.findByIdOrNull(membershipInputDTO.roleId) } returns role
        every { membershipRepository.findByTeamIdAndMemberId(membershipInputDTO.teamId, membershipInputDTO.memberId) } returns existingMembership

        assertThrows<ResourceAlreadyExistsException> { membershipService.assignRoleToMember(membershipInputDTO) }
    }

    @Test
    fun `should throw IllegalAssignmentException when membership is not part of the team or team lead`() {
        val randomId = randomUUIDString()
        val membershipInputDTO = createMembershipInputDTO().copy(memberId = randomId)
        val team = createTeam(membershipInputDTO.teamId, listOf(randomUUIDString()))
        val member = createUser(randomId)
        val roleId = membershipInputDTO.roleId!!
        val role = createRole(roleId)

        every { teamRepository.getTeamByIdOrNull(membershipInputDTO.teamId) } returns team
        every { userRepository.getUserByIdOrNull(membershipInputDTO.memberId) } returns member
        every { roleRepository.findByIdOrNull(membershipInputDTO.roleId) } returns role

        assertThrows<IllegalAssignmentException> { membershipService.assignRoleToMember(membershipInputDTO) }
    }

    @Test
    fun `should filter memberships based on membership filter`() {
        val membershipFilterDTO = createMembershipFilterDTO()
        val memberships = listOf(
            createMembership(id = randomUUIDString(),  teamId = "team-1", memberId = "member-1", roleId = "role-1"),
            createMembership(id = randomUUIDString(), teamId = "team-2", memberId = "member-2", roleId = "role-2"),
            createMembership(id = randomUUIDString(), teamId = "team-3", memberId = "member-3", roleId = "role-3")
        )
        every { membershipRepository.findAll(any<Specification<Membership>>()) } returns memberships

        val result = membershipService.filterMemberships(membershipFilterDTO)

        assertEquals(memberships.map { it.toDTO() }, result)
    }

    private fun createMembershipFilterDTO(): MembershipFilterDTO {
        return MembershipFilterDTO(
            teamId = "team-1",
            memberId = "member-1",
            roleId = "role-1",
            id = "membership-1"
        )
    }

    private fun createMembershipInputDTO(): MembershipInputDTO {
        return MembershipInputDTO(
            teamId = randomUUIDString(),
            memberId = randomUUIDString(),
            roleId = randomUUIDString()
        )
    }

    private fun createTeam(id: String, teamMemberIds: List<String>): TeamTO {
        return TeamTO(
            id = id,
            name = "Team 1",
            teamLeadId = randomUUIDString(),
            teamMemberIds = teamMemberIds
        )
    }

    private fun createUser(id: String): UserTO {
        return UserTO(
            id = id,
            firstName = "John",
            lastName = "Doe",
            displayName = "John Doe",
            avatarUrl = "",
            location = ""
        )
    }

    private fun createRole(id: String): Role {
        return Role(
            id = id,
            name = "Role 1"
        )
    }

    private fun createMembership(id: String? = null, teamId: String, memberId: String, roleId: String): Membership {
        return Membership(
            id = id,
            teamId = teamId,
            memberId = memberId,
            roleId = roleId
        )
    }

    companion object {
        private fun randomUUIDString() = UUID.randomUUID().toString()
    }
}
