package com.tempo.assessment.rolesapp.service

import com.tempo.assessment.rolesapp.model.RoleDTO
import com.tempo.assessment.rolesapp.model.RoleInputDTO
import com.tempo.assessment.rolesapp.model.extensions.toDTO
import com.tempo.assessment.rolesapp.model.extensions.toEntity
import com.tempo.assessment.rolesapp.repository.RoleRepository
import com.tempo.assessment.rolesapp.repository.entity.Role
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class RoleServiceImplTest {

    private val roleRepository: RoleRepository = mockk()
    private val roleService = RoleServiceImpl(roleRepository)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `should return all roles`() {
        val roles = listOf(createRole("role-1"), createRole("role-2"), createRole("role-3"))
        every { roleRepository.findAll() } returns roles

        val result = roleService.getRoles()

        assertEquals(roles.size, result.size)
        assertEquals(roles.map { it.toDTO() }, result)
    }

    @Test
    fun `should return role by id`() {
        val roleId = randomUUIDString()
        val role = createRole(roleId)
        every { roleRepository.findByIdOrNull(roleId) } returns role

        val result = roleService.getRoleById(roleId)

        assertEquals(role.toDTO(), result)
    }

    @Test
    fun `should return null when role by id is not found`() {
        val roleId = randomUUIDString()
        every { roleRepository.findByIdOrNull(roleId) } returns null

        val result = roleService.getRoleById(roleId)

        assertEquals(null, result)
    }

    @Test
    fun `should create role`() {
        val roleDTO = RoleInputDTO(name ="Role 1")
        val id = randomUUIDString()
        every { roleRepository.save(any()) } answers { value.copy(id = id) }

        val result = roleService.createRole(roleDTO)

        assertEquals(id, result.id)
        assertEquals(roleDTO.name, result.name)
    }

    private fun createRole(id: String): Role {
        return Role(
            id = id,
            name = "Role $id"
        )
    }

    companion object {
        private fun randomUUIDString() = UUID.randomUUID().toString()
    }
}