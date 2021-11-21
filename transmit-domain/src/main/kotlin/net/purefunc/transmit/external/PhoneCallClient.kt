package net.purefunc.transmit.external

import net.purefunc.common.domain.data.Result

interface PhoneCallClient {

    fun send(phone: String, content: String): Result<String>
}
