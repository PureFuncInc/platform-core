package net.funpodium.member.domain.data.entity

import net.funpodium.member.domain.data.type.Password
import net.funpodium.member.domain.data.type.Status
import net.funpodium.member.domain.exception.InvalidDataFormatException
import net.funpodium.member.external.MemberDataFormatValidator

data class Member(

    val id: Long?,

    val username: String,

    val email: String,

    val password: Password,

    val status: Status,

    val createBy: String,

    val updateBy: String,

    val createDate: Long,

    val updateDate: Long,

    val lastUpdatePasswordDate: Long,
) {

    fun valid(memberDataFormatValidator: MemberDataFormatValidator) =
        apply {
            if (memberDataFormatValidator.isUnvalidUsername(username)) throw InvalidDataFormatException(username)
            if (memberDataFormatValidator.isUnvalidPassword(password.raw!!)) throw InvalidDataFormatException(password.raw)
            if (memberDataFormatValidator.isUnvalidEmail(email)) throw InvalidDataFormatException(email)
        }
}
