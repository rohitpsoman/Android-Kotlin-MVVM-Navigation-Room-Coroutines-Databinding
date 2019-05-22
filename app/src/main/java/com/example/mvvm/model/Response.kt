package com.example.mvvm.model


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Error<out T>(val throwable: Throwable?,val data: T? = null) : Response<T>()
    data class Loading(val status: Boolean) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable]"
            is Loading -> "Loading[status=$status]"
        }
    }
}

/**
 * `true` if [Response] is of type [Success] & holds non-null [Success.data].
 */
val Response<*>.succeeded
    get() = this is Response.Success && data != null
