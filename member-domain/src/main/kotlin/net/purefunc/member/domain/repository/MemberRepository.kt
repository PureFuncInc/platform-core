package net.purefunc.member.domain.repository

import arrow.core.Either
import net.purefunc.member.domain.data.entity.Member

interface MemberRepository {

    suspend fun queryOrPersist(member: Member): Either<Throwable, Member>
}
