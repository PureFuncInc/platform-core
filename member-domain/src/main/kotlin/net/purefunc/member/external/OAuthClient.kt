package net.purefunc.member.external

import net.purefunc.member.domain.data.entity.Member

interface OAuthClient {

    fun fetch(code: String, ttlSeconds: Long): Member?
}
