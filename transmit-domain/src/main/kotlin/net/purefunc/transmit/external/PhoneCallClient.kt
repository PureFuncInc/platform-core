package net.purefunc.transmit.external

import net.purefunc.core.domain.data.Result

interface PhoneCallClient {

    fun send(phone: String, content: String): Result<String>
}
