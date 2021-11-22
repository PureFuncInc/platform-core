package net.funpodium.member.domain.repository

import net.purefunc.core.domain.data.type.Page
import net.funpodium.member.domain.data.entity.Member
import net.funpodium.member.domain.data.type.Password
import net.funpodium.member.domain.data.type.Status

interface MemberRepository {

    fun findPasswordByUsername(username: String): Password?

    fun findPasswordByEmail(email: String): Password?

    fun save(member: Member): Member

    fun findEmailByUsername(username: String): String?

    fun findPhoneByUsername(username: String): String?

    fun updatePasswordById(newPassword: String, id: Long): Int

    fun findAllOrderByIdDesc(page: Page): List<Member>

    fun findByUsername(username: String): Member?

    fun findByEmail(email: String): Member?

    fun findByPhone(phone: String): Member?

    fun findPasswordById(id: Long): Password?
    fun updateStatusById(status: Status, id: Long): Int
}
