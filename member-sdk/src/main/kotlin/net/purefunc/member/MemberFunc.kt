package net.purefunc.member

import net.purefunc.member.data.vo.JwtToken
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.domain.data.type.Status
import java.util.UUID

open class MemberFunc {

    fun initMember(
        name: String,
        ttlSeconds: Long,
        role: String,
        email: String,
    ) = JwtToken.generate(
        id = UUID.randomUUID().toString(),
        subject = name,
        issueAt = System.currentTimeMillis(),
        expiration = System.currentTimeMillis() + (ttlSeconds * 60L * 60L * 1000L),
    ).let {
        Member(
            id = null,
            token = it,
            name = name,
            email = email,
            role = role,
            status = Status.ACTIVE,
            lastLoginDate = System.currentTimeMillis(),
        )
    }
}