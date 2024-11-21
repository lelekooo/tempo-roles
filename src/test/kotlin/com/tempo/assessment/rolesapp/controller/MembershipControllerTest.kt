package com.tempo.assessment.rolesapp.controller

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.tempo.assessment.rolesapp.RolesAppApplication
import com.tempo.assessment.rolesapp.model.MembershipInputDTO
import com.tempo.assessment.rolesapp.repository.MembershipRepository
import com.tempo.assessment.rolesapp.repository.RoleRepository
import com.tempo.assessment.rolesapp.repository.entity.Role
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.context.WebApplicationContext
import java.util.*


@ExtendWith(SpringExtension::class)
@WireMockTest(httpPort = 8081)
@SpringBootTest(classes = [RolesAppApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MembershipControllerTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    lateinit var membershipRepository: MembershipRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Value("\${tempo.defaultRoleId}")
    lateinit var defaultRoleId: String


    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun setup() {
        membershipRepository.deleteAll()
        roleRepository.save(Role(id = defaultRoleId, name = "Developer"))
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    private val membershipsURL = "http://localhost:$port/membership/"

    /*
        Not all the cases where covered in the integration tests by the lack of time. But follow an example using Wiremock
        for representing external integrations scenarios.
     */
    @Test
    fun `should create membership`() {
        val role = roleRepository.save(Role(name = "Role 1"))
        val membershipInputDTO = MembershipInputDTO(
            teamId = "7676a4bf-adfe-415c-941b-1739af07039b", //id from teams.json
            memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c", //id from users.json
            roleId = role.id!!
        )
        Given {
            webAppContextSetup(webApplicationContext)
            contentType(MediaType.APPLICATION_JSON)
            body(membershipInputDTO)
        } When {
            post(membershipsURL)
        } Then {
            body("id", Matchers.notNullValue())
            body("teamId", Matchers.equalTo("7676a4bf-adfe-415c-941b-1739af07039b"))
            body("roleId", Matchers.equalTo(role.id))
            body("memberId", Matchers.equalTo("fd282131-d8aa-4819-b0c8-d9e0bfb1b75c"))
        }
    }
}

