package net.purefunc.member.domain.data.entity

import net.purefunc.member.domain.data.type.Status
import java.util.UUID

data class Member(

    val id: Long?,

    var token: String,

    var name: String,

    var email: String,

    var status: Status,

    var role: String,

    var lastLoginDate: Long,
)