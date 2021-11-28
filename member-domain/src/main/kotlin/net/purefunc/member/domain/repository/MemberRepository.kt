package net.purefunc.member.domain.repository

import net.purefunc.member.domain.data.entity.Member

interface MemberRepository {

    fun persist(member: Member): Member

    fun queryByEmail(email: String): Member?
}
