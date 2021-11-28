package net.purefunc.member.ext

import net.purefunc.member.data.vo.JwtToken
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.domain.data.type.Status
import java.util.UUID

fun genMemberBy(
    name: String,
    ttlSeconds: Long,
    email: String,
) = run {
    val token = JwtToken.generate(
        id = UUID.randomUUID().toString(),
        subject = name,
        issueAt = System.currentTimeMillis(),
        expiration = System.currentTimeMillis() + (ttlSeconds * 60L * 60L * 1000L),
    )

    Member(
        id = null,
        token = token,
        name = name,
        email = email,
        role = "USER",
        status = Status.ACTIVE,
        lastLoginDate = System.currentTimeMillis(),
    )
}