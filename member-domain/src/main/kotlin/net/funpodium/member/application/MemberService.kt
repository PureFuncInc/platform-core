package net.funpodium.member.application

import net.funpodium.common.domain.data.type.Operation
import net.funpodium.common.domain.data.type.Page
import net.funpodium.member.domain.data.entity.Member
import net.funpodium.member.domain.data.type.EMail
import net.funpodium.member.domain.data.type.Status

interface MemberService {

    // product

    fun login(username: String, password: String): Operation

    fun login(email: EMail, password: String): Operation

    fun signup(email: String): Operation

    fun logout(userId: Long): Operation

//    fun signup(member: Member): Member

    fun forgetPasswordResetByEmail(username: String): String

    fun forgetPasswordResetByPhone(username: String): String

    fun modifyPasswordById(oldPassword: String, newPassword: String, id: Long): Int

    // back office

    fun queryAllOrderByIdDesc(page: Page): List<Member>

    fun queryByUsername(username: String): Member

    fun queryByEmail(email: String): Member

    fun queryByPhone(phone: String): Member
    fun modifyStatusById(status: Status, id: Long): Int
}
