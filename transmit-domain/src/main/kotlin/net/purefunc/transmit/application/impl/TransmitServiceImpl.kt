package net.purefunc.transmit.application.impl

import net.purefunc.transmit.external.EmailClient
import net.purefunc.transmit.external.PhoneCallClient
import net.purefunc.transmit.external.SmsClient
import transmit.TransmitService

class TransmitServiceImpl(
    private val emailClient: EmailClient,
    private val smsClient: SmsClient,
    private val phoneCallClient: PhoneCallClient,
) : TransmitService {

    override fun sendEmail(subject: String, personal: String, address: String, htmlContent: String) =
        emailClient.send(subject, personal, address, htmlContent)

    override fun sendSms(phone: String, content: String) = smsClient.send(phone, content)

    override fun makePhoneCall(phone: String, content: String) = phoneCallClient.send(phone, content)
}