package net.funpodium.member.domain.data.type

import net.purefunc.core.domain.data.type.BusinessOperation
import net.funpodium.member.external.PasswordEncoder

data class Password(

    val raw: String?,

    val hash: String,
) {

    fun valid(password: String, passwordEncoder: PasswordEncoder) =
        if (passwordEncoder.hash(password) == hash) BusinessOperation.LOGIN_SUCCESS
        else BusinessOperation.LOGIN_FAIL

    fun raw2Hash(passwordEncoder: PasswordEncoder) = this.copy(raw = raw, hash = passwordEncoder.hash(raw!!))
}
