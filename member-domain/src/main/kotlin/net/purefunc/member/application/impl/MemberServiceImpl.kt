package net.purefunc.member.application.impl

import net.purefunc.member.application.MemberService
import net.purefunc.member.domain.repository.MemberRepository
import net.purefunc.member.external.OAuthClient

class MemberServiceImpl(
    private val oauthClient: OAuthClient,
    private val memberRepository: MemberRepository,
) : MemberService {

    override suspend fun fetchVia(code: String, ttlSeconds: Long) =
        oauthClient.fetch(code, ttlSeconds)
            ?.run {
                (memberRepository.queryByEmail(email) ?: memberRepository.persist(this)).copy(token = token)
            }
}
