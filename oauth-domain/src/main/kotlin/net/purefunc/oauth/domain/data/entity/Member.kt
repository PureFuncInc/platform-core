package net.purefunc.oauth.domain.data.entity

data class Member(

    val id: Long?,

    var name: String,

    var email: String,

    var role: String,

    var lastLoginDate: Long,
)