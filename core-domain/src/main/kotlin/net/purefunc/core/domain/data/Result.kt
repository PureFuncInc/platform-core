package net.purefunc.core.domain.data

// Result is a superpowered enum that can be Success or Failure
// and the basis for a railway junction
sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val cause: Throwable) : Result<T>()

// Composition: apply a function f to Success results
infix fun <T, U> Result<T>.then(f: (T) -> Result<U>) =
    when (this) {
        is Success -> f(this.value)
        is Failure -> Failure(this.cause)
    }

// Pipe input: the beginning of a railway
infix fun <T, U> T.to(f: (T) -> Result<U>) =
    Success(this) then f

// Handle error output: the end of a railway
infix fun <T> Result<T>.otherwise(f: (Throwable) -> Unit) =
    if (this is Failure) f(this.cause) else Unit

fun <T> tryOrFailure(block: () -> T) =
    try {
        Success(block.invoke())
    } catch (ex: Exception) {
        Failure(ex.cause!!)
    }