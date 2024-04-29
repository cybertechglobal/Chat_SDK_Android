package eu.brrm.chatui.internal.network

internal abstract class Response<T>(
    val statusCode: Int = 200,
    val data: T? = null,
    val error: Throwable? = null
) {
    abstract fun isSuccess(): Boolean

    internal class Success<T>(statusCode: Int, data: T?) : Response<T>(statusCode, data) {
        override fun isSuccess(): Boolean = true
    }

    internal class Error<T>(statusCode: Int, throwable: Throwable?) :
        Response<T>(statusCode, null, throwable) {
        override fun isSuccess(): Boolean = false
    }
}