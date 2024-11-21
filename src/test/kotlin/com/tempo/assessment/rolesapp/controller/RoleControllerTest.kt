package com.tempo.assessment.rolesapp.controller

import com.tempo.assessment.rolesapp.RolesAppApplication
import com.tempo.assessment.rolesapp.model.RoleDTO
import com.tempo.assessment.rolesapp.model.RoleInputDTO
import com.tempo.assessment.rolesapp.repository.RoleRepository
import com.tempo.assessment.rolesapp.repository.entity.Role
import io.restassured.RestAssured
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [RolesAppApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleControllerTest {

    @Autowired lateinit var webApplicationContext: WebApplicationContext
    @Autowired lateinit var roleRepository: RoleRepository
    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun setup() {
        roleRepository.deleteAll()
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    private val rolesURL = "http://localhost:$port/role/"

    @Test
    fun `should return all roles`() {
        roleRepository.saveAll(listOf(Role(name = "Role 1"), Role(name = "Role 2")))

        Given {
            webAppContextSetup(webApplicationContext)
        } When {
            get(rolesURL)
        } Then {
            body("$.size()", Matchers.equalTo(2))
            body("[0].name", Matchers.equalTo("Role 1"))
            body("[1].name", Matchers.equalTo("Role 2"))
        }
    }

    @Test
    fun `should return role by id`() {
        val role = roleRepository.save(Role(name = "Role 1"))

        Given {
            webAppContextSetup(webApplicationContext)
        } When {
            get("$rolesURL${role.id}")
        } Then {
            body("id", Matchers.equalTo(role.id))
            body("name", Matchers.equalTo(role.name))
        }
    }

    @Test
    fun `should create role`() {
        val roleInputDTO = RoleInputDTO("Role 1")

        Given {
            webAppContextSetup(webApplicationContext)
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(roleInputDTO)
        } When {
            post("$rolesURL")
        } Then {
            body("id", Matchers.notNullValue())
            body("name", Matchers.equalTo("Role 1"))
        }

    }

}

