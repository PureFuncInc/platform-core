package net.purefunc.transmit.sdk

import net.purefunc.common.domain.data.Success
import net.purefunc.common.domain.data.otherwise
import net.purefunc.common.ext.Slf4j
import net.purefunc.common.ext.Slf4j.Companion.log
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@Slf4j
class GmailClientTests {

    @Test
    internal fun testSend() {
        val userName = System.getenv("userName")
        val password = System.getenv("password")

        val gmailClient = GmailClient(userName, password)
        val result = gmailClient.send(
            subject = "測試Mail 123",
            personal = "\$_ purefunc",
            address = "yfr.huang@hotmail.com",
            htmlContent = "<h1>Hello</h1><p>We're PureFunc</p>"
        )
        result.otherwise {
            log.error(it.message, it)
        }

        Assertions.assertThat(result is Success).isTrue
    }
}