package com.jans.societyoo.utils

const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

sealed class MyResult<out T : Any> {

    class Success<out T : Any>(val data: T) : MyResult<T>()

    class SuccessAndLoading<out T : Any>(val data: T) : MyResult<T>()

    class Error(
        val exception: Throwable,
        val message: String = exception.message ?: UNKNOWN_ERROR
    ) : MyResult<Nothing>()

    object Loading : MyResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is SuccessAndLoading<*> -> "SuccessAndLoading[data=$data]"
            is Error -> "Error[exception=$exception, message=$message]"
            Loading -> "Loading"
        }
    }

    inline fun <Mapped : Any> map(transform: (data: T) -> Mapped): MyResult<Mapped> {
        return when (this) {
            is Success -> Success(transform(data))
            is SuccessAndLoading -> SuccessAndLoading(transform(data))
            is Error -> this
            is Loading -> this
        }
    }
}

/*
* R :- if success then value will be returned
*       if value null then NULL Pointer Exception
*       else throw appropriate error
**/
inline fun <R : Any> tryCatching(block: () -> R): MyResult<R> {
    return try {
        MyResult.Success(block())
    } catch (e: Throwable) {
        MyResult.Error(e)
    }
}