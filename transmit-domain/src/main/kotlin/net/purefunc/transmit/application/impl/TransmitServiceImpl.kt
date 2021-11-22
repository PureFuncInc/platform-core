package net.purefunc.transmit.application.impl

import net.purefunc.core.domain.data.Failure
import net.purefunc.core.domain.data.Success
import net.purefunc.core.domain.data.then
import net.purefunc.transmit.external.EmailClient
import net.purefunc.transmit.external.PhoneCallClient
import net.purefunc.transmit.external.SmsClient
import net.purefunc.core.transmit.TransmitService

class TransmitServiceImpl(
    private val emailClient: EmailClient,
    private val smsClient: SmsClient,
    private val phoneCallClient: PhoneCallClient,
) : TransmitService {

    private val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$".toRegex()

    override fun sendEmail(
        subject: String,
        personal: String,
        address: String,
        htmlContent: String
    ) = validateParams(subject, personal, address, htmlContent)
        .then {
            emailClient.send(subject, personal, address, htmlContent)
        }

    private fun validateParams(
        subject: String,
        personal: String,
        address: String,
        htmlContent: String
    ) = when {
        subject.isBlank() || personal.isBlank() || htmlContent.isBlank() -> Failure(RuntimeException(""))
        !emailRegex.matches(address) -> Failure(RuntimeException(""))
        else -> Success("")
    }

    override fun sendSms(phone: String, content: String) = smsClient.send(phone, content)

    override fun makePhoneCall(phone: String, content: String) = phoneCallClient.send(phone, content)
}