package net.purefunc.member.application.impl

import arrow.core.flatMap
import net.purefunc.member.application.MemberService
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.domain.repository.MemberRepository
import net.purefunc.member.external.OAuthClient
import net.purefunc.member.external.TotpClient

class MemberServiceImpl(
    private val oauthClient: OAuthClient,
    private val totpClient: TotpClient,
    private val memberRepository: MemberRepository,
) : MemberService {

    override suspend fun oauthFetch(
        code: String,
        ttlSeconds: Long,
        role: String,
    ) = oauthClient.fetch(code, ttlSeconds, role)
        .flatMap { member -> memberRepository.queryOrPersist(member).map { it.copy(token = member.token) } }

    override suspend fun totpGenQrCode(
        member: Member,
        issuer: String,
    ) = memberRepository.queryOrUpdateSecret(member)
        .flatMap { totpClient.genQrCode(member, issuer) }

    override suspend fun totpVerifyCode(
        member: Member,
        code: String
    ) = memberRepository.query(member)
        .flatMap { totpClient.verifyCode(it, code) }
}