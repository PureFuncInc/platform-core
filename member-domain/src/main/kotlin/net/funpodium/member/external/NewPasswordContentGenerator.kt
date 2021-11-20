package net.funpodium.member.external

interface NewPasswordContentGenerator {

    fun signupEmail(): String

    fun newEmail(username: String): String

    fun newSms(username: String): String
}
