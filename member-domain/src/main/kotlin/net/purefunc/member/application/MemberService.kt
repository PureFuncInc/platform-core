package net.purefunc.member.application

import net.purefunc.member.domain.data.entity.Member

interface MemberService {

    suspend fun fetchVia(code: String, ttlSeconds: Long): Member?
}
