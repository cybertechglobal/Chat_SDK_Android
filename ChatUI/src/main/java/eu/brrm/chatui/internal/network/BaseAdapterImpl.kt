package eu.brrm.chatui.internal.network

import org.json.JSONObject

internal class BaseAdapterImpl<T>(private val parser: (JSONObject?) -> T) : Adapter<T> {
    override fun createResponse(
        statusCode: Int,
        data: JSONObject?,
        error: Throwable?
    ): Response<T> {
        return if (error == null) {
            Response.Success(statusCode = statusCode, data = parser(data))
        } else {
            Response.Error(statusCode = statusCode, throwable = error)
        }
    }
}