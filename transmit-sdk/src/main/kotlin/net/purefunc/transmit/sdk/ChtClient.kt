package net.purefunc.transmit.sdk

import net.purefunc.core.domain.data.Failure
import net.purefunc.core.domain.data.Result
import net.purefunc.core.domain.data.Success
import net.purefunc.transmit.external.SmsClient
import net.purefunc.transmit.sms.CHT
import java.nio.charset.Charset

class ChtClient(
    private val userName: String,
    private val password: String,
) : SmsClient {

    override fun send(phone: String, content: String): Result<String> {
        val cht = CHT()
        val connResult = cht.create_conn(userName, password)
        if (connResult != 0) {
            cht.close_conn()
            return Failure(RuntimeException(""))
        }

        val sendResult = cht.send_text_message(phone, String(content.toByteArray(), Charset.forName("big5")))
        try {
            return if (sendResult == 0) {
                Success(cht._message)
            } else {
                Failure(RuntimeException(""))
            }
        } finally {
            cht.close_conn()
        }
    }
}