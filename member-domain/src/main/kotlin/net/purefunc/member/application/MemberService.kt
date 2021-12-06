package net.purefunc.member.application

import arrow.core.Either
import net.purefunc.member.domain.data.entity.Member

interface MemberService {

    suspend fun oauthFetch(code: String, ttlSeconds: Long, role: String): Either<Throwable, Member>

    suspend fun totpGenQrCode(member: Member, issuer: String): Either<Throwable, ByteArray>

    suspend fun totpVerifyCode(member: Member, code: String): Either<Throwable, Boolean>
}
