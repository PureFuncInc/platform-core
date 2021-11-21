package net.funpodium.member.application

import net.purefunc.common.domain.data.type.BusinessOperation
import net.purefunc.common.domain.data.type.Page
import net.funpodium.member.domain.data.entity.Member
import net.funpodium.member.domain.data.type.EMail
import net.funpodium.member.domain.data.type.Status

interface MemberService {

    // product

//    fun login(username: String, password: String): BusinessOperation
//
//    fun login(email: EMail, password: String): BusinessOperation
//
//    fun signup(email: String): BusinessOperation
//
//    fun logout(userId: Long): BusinessOperation

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
