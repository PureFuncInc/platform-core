package net.purefunc.transmit.application

import arrow.core.Either

interface TransmitService {

    fun sendEmail(subject: String, personal: String, address: String, htmlContent: String): Either<Exception, Unit>

    fun sendSms(phone: String, content: String): Either<Exception, Unit>

    fun makePhoneCall(phone: String, content: String): Either<Exception, Unit>
}