package eu.brrm.chatui.internal.network

import org.json.JSONObject

internal interface Adapter<T> {
    fun createResponse(
        statusCode: Int,
        data: JSONObject? = null,
        error: Throwable? = null,
    ): Response<T>
}