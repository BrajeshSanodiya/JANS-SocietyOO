package com.jans.societyoo.data.remote

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResultOld<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultOld<T>()
    data class Error(val exception: Exception) : ResultOld<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
