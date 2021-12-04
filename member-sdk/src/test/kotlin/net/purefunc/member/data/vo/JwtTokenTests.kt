package net.purefunc.member.data.vo

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class JwtTokenTests {

    @Test
    internal fun `test generate and retrieve subject`() {
        val id = UUID.randomUUID().toString()
        val subject = "purefuncinc@gmail.com"
        val issueAt = System.currentTimeMillis()
        val expiration = issueAt + (60 * 1000)

        val jwtToken = JwtToken.generate(
            id = id,
            subject = subject,
            issueAt = issueAt,
            expiration = expiration
        )

        val retrieveSubject = JwtToken.retrieveSubject(jwtToken)

        Assertions.assertThat(retrieveSubject).isEqualTo(subject)
    }
}