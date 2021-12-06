package net.purefunc.member.external

import arrow.core.Either
import net.purefunc.member.domain.data.entity.Member

interface TotpClient {

    suspend fun genQrCode(member: Member, issuer: String): Either<Throwable, ByteArray>

    suspend fun verifyCode(member: Member, code: String): Either<Throwable, Boolean>
}
