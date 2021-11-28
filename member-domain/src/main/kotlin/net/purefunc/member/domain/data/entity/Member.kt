package net.purefunc.member.domain.data.entity

import net.purefunc.member.domain.data.type.Status

data class Member(

    val id: Long?,

    val token: String,

    val name: String,

    val email: String,

    val status: Status,

    val role: String,

    val lastLoginDate: Long,
)