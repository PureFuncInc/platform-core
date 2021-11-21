package net.purefunc.transmit.external

import net.purefunc.common.domain.data.Result

interface EmailClient {

    fun send(subject: String, personal: String, address: String, htmlContent: String): Result<Unit>
}
