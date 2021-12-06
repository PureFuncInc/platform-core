package net.purefunc.member.external.impl

import dev.samstevens.totp.secret.DefaultSecretGenerator
import kotlinx.coroutines.runBlocking
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.domain.data.type.Status
import org.assertj.core.api.Assertions
import org.junit.Ignore
import org.junit.jupiter.api.Test

class DefaultTotpClientTests {

    private val member = Member(
        id = null,
        token = "",
        name = "純函式股份有限公司",
        email = "purefuncinc@gmail.com",
        role = "",
        status = Status.ACTIVE,
        secret = DefaultSecretGenerator(64).generate(),
        lastLoginDate = System.currentTimeMillis(),
    )

    @Test
    internal fun `test generate qr code`() {
        runBlocking {
//            println(member.secret)

            DefaultTotpClient().genQrCode(member, "purefunc.net")
                .fold(
                    ifLeft = { println(it.message) },
                    ifRight = {
//                        ImageIO.write(ImageIO.read(ByteArrayInputStream(it)), "png", File("./qrcode.png"))
                        Assertions.assertThat(it.isNotEmpty()).isTrue
                    }
                )
        }
    }

    @Ignore
    @Test
    internal fun `test verify code`() {
        runBlocking {
            DefaultTotpClient().verifyCode(member.copy(secret = "WEZDKYCQG7TDPOQRIZIKPAVG74L5PTAPD3DBEX7XGC6MJPD4IKWF5O3YL3C5VEAR"),
                "449848")
                .fold(
                    ifLeft = { println(it.message) },
                    ifRight = { Assertions.assertThat(it).isTrue }
                )
        }
    }
}