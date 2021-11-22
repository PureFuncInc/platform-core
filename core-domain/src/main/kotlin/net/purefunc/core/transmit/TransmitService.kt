package net.purefunc.core.transmit

import net.purefunc.core.domain.data.Result

interface TransmitService {

    fun sendEmail(subject: String, personal: String, address: String, htmlContent: String): Result<Unit>

    fun sendSms(phone: String, content: String): Result<String>

    fun makePhoneCall(phone: String, content: String): Result<String>
}