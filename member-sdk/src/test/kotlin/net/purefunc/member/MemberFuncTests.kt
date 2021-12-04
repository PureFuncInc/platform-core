package net.purefunc.member

import net.purefunc.member.domain.data.type.Status
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MemberFuncTests : MemberFunc() {

    @Test
    internal fun `test init member`() {
        val name = "純函式股份有限公司"
        val ttlSeconds = 60L * 60L
        val role = "BO"
        val email = "purefuncinc@gmail.com"

        val member = initMember(
            name = name,
            ttlSeconds = ttlSeconds,
            role = role,
            email = email,
        )

        Assertions.assertThat(member.token).isNotBlank
        Assertions.assertThat(member.name).isEqualTo(name)
        Assertions.assertThat(member.email).isEqualTo(email)
        Assertions.assertThat(member.role).isEqualTo(role)
        Assertions.assertThat(member.status).isEqualTo(Status.ACTIVE)
        Assertions.assertThat(member.lastLoginDate <= System.currentTimeMillis()).isTrue
    }
}