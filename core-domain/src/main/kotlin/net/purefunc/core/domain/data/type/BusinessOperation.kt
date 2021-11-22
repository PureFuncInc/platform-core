package net.purefunc.core.domain.data.type

data class BusinessOperation<T>(

    val code: String,

    val message: String,

    val body: T,
) {

    companion object {
        val LOGIN_SUCCESS = BusinessOperation("MEM_1_1101", "LOGIN_SUCCESS", "")
        val LOGIN_FAIL = BusinessOperation("MEM_0_1102", "LOGIN_FAIL", "")
        val SIGNUP_SUCCESS = BusinessOperation("MEM_1_2001", "SIGNUP_SUCCESS", "")
        val SIGNUP_FAIL = BusinessOperation("MEM_1_2002", "SIGNUP_FAIL", "")
        val LOGOUT_SUCCESS = BusinessOperation("MEM_1_3001", "LOGOUT_SUCCESS", "")
        val RESET_PASSWORD_FAIL = BusinessOperation("MEM_0_022", "RESET_PASSWORD_FAIL", "")
        val MODIFY_PASSWORD_FAIL = BusinessOperation("MEM_0_003", "MODIFY_PASSWORD_FAIL", "")
    }
}
