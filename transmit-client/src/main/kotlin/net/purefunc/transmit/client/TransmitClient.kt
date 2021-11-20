package net.purefunc.transmit.client

interface TransmitClient {

    fun sendEmail(email: String, content: String): String

    fun sendSms(phone: String, content: String): String

    fun makePhoneCall(phone: String, content: String): String
}