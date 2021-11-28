package net.purefunc.member.external

import net.purefunc.member.domain.data.entity.Member

interface OAuthClient {

    fun fetch(accessToken: String, jwtTtlSeconds: Long): Member?
}
