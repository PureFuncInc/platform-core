package net.purefunc.member.domain.repository

import arrow.core.Either
import net.purefunc.member.domain.data.entity.Member

interface MemberRepository {

    suspend fun queryOrPersist(member: Member): Either<Throwable, Member>

    suspend fun queryOrUpdateSecret(member: Member): Either<Throwable, Member>

    suspend fun query(member: Member): Either<Throwable, Member>
}
