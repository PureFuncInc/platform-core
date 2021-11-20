package net.funpodium.member.external

interface PasswordEncoder {

    fun hash(raw: String): String
}
