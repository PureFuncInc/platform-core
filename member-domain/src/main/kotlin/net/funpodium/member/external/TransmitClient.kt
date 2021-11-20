package net.funpodium.member.external

interface TransmitClient {

    fun sendEmail(email: String, content: String): String

    fun sendSms(phone: String, content: String): String
}
