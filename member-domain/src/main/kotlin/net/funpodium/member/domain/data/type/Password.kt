package net.funpodium.member.domain.data.type

import net.funpodium.common.domain.data.type.Operation
import net.funpodium.member.domain.exception.WrongPasswordException
import net.funpodium.member.external.PasswordEncoder

data class Password(

    val raw: String?,

    val hash: String,
) {

    fun valid(password: String, passwordEncoder: PasswordEncoder) =
        if (passwordEncoder.hash(password) == hash) Operation.LOGIN_SUCCESS
        else Operation.LOGIN_FAIL

    fun raw2Hash(passwordEncoder: PasswordEncoder) = this.copy(raw = raw, hash = passwordEncoder.hash(raw!!))
}
