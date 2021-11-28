package net.purefunc.member.domain.repository

import net.purefunc.member.domain.data.entity.Member

interface MemberRepository {

    suspend fun persist(member: Member): Member

    suspend fun queryByEmail(email: String): Member?
}
