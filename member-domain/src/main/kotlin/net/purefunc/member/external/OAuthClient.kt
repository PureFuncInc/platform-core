package net.purefunc.member.external

import arrow.core.Either
import net.purefunc.member.domain.data.entity.Member

interface OAuthClient {

    suspend fun fetch(accessToken: String, jwtTtlSeconds: Long, role: String): Either<Throwable, Member>
}
