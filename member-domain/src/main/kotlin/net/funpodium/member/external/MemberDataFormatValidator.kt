package net.funpodium.member.external

interface MemberDataFormatValidator {

    fun isUnvalidUsername(username: String): Boolean

    fun isUnvalidPassword(rawPassword: String): Boolean

    fun isUnvalidEmail(email: String): Boolean

    fun isUnvalidPhone(phone: String): Boolean
}
