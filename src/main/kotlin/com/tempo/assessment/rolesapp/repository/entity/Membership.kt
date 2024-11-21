package com.tempo.assessment.rolesapp.repository.entity

import jakarta.persistence.*

@Entity
@Table(name = "membership")
data class Membership(
    var teamId: String,
    var memberId: String,
    var roleId: String,
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    var role: Role? = null
)
