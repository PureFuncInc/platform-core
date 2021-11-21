package net.purefunc.common.domain.data

// Result is a superpowered enum that can be Success or Failure
// and the basis for a railway junction
sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val code: String, val message: T) : Result<T>()

// Composition: apply a function f to Success results
infix fun <T, U> Result<T>.then(f: (T) -> Result<U>) =
    when (this) {
        is Success -> f(this.value)
        is Failure -> Failure(this.code, this.message)
    }

// Pipe input: the beginning of a railway
infix fun <T, U> T.to(f: (T) -> Result<U>) =
    Success(this) then f

// Handle error output: the end of a railway
//infix fun <T> Result<T>.otherwise(f: (String, String) -> Unit) =
//    if (this is Failure) f(this.code, this.message) else Unit