package net.purefunc.member.application.impl

import arrow.core.flatMap
import net.purefunc.member.application.MemberService
import net.purefunc.member.domain.repository.MemberRepository
import net.purefunc.member.external.OAuthClient

class MemberServiceImpl(
    private val oauthClient: OAuthClient,
    private val memberRepository: MemberRepository,
) : MemberService {

    override suspend fun fetchVia(
        code: String,
        ttlSeconds: Long,
        role: String,
    ) = oauthClient.fetch(code, ttlSeconds, role)
        .flatMap { member ->
            memberRepository.queryOrPersist(member).map { it.copy(token = member.token) }
        }
}