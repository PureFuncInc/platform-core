package net.purefunc.transmit.sdk

import net.purefunc.common.domain.data.Success
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GmailClientTests {

    @Test
    internal fun testSend() {
        val userName = System.getenv("userName")
        val password = System.getenv("password")

        val gmailClient = GmailClient(userName, password)
        val result = gmailClient.send("最新的測試Mail來囉", "yfr.huang@hotmail.com", "<h1>Hello</h1><p>We're PureFunc</p>")

        Assertions.assertThat(result is Success).isTrue
    }
}