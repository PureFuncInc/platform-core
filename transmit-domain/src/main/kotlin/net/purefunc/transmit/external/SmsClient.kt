package net.purefunc.transmit.external

import net.purefunc.core.domain.data.Result

interface SmsClient {

    fun send(phone: String, content: String): Result<String>
}