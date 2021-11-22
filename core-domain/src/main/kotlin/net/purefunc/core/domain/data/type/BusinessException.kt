package net.purefunc.core.domain.data.type

class BusinessException : RuntimeException {

    private val errorCode: ErrorCode

    constructor(code: ErrorCode) : super(code.message) {
        this.errorCode = code
    }

    constructor(code: ErrorCode, cause: Throwable) : super(code.message, cause) {
        this.errorCode = code
    }
}