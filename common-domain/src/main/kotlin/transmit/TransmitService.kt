package transmit

import net.purefunc.common.domain.data.Result

interface TransmitService {

    fun sendEmail(subject: String, address: String, htmlContent: String): Result<String>

    fun sendSms(phone: String, content: String): Result<String>

    fun makePhoneCall(phone: String, content: String): Result<String>
}