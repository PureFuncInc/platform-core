package net.funpodium.common.domain.data.type

data class Operation(

    val code: String,

    val message: String,
) {

    companion object {
        val LOGIN_SUCCESS = Operation("MEM_1_1101", "LOGIN_SUCCESS")
        val LOGIN_FAIL = Operation("MEM_0_1102", "LOGIN_FAIL")
        val SIGNUP_SUCCESS = Operation("MEM_1_2001", "SIGNUP_SUCCESS")
        val SIGNUP_FAIL = Operation("MEM_1_2002", "SIGNUP_FAIL")
        val LOGOUT_SUCCESS = Operation("MEM_1_3001", "LOGOUT_SUCCESS")
        val RESET_PASSWORD_FAIL = Operation("MEM_0_022", "RESET_PASSWORD_FAIL")
        val MODIFY_PASSWORD_FAIL = Operation("MEM_0_003", "MODIFY_PASSWORD_FAIL")
    }
}
