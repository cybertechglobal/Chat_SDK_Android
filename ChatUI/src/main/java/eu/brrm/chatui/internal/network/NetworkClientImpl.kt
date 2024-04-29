package eu.brrm.chatui.internal.network

import eu.brrm.chatui.ChatLog.logDebug
import eu.brrm.chatui.ChatLog.logError
import eu.brrm.chatui.ChatLog.logInfo
import eu.brrm.chatui.internal.storage.Storage
import eu.brrm.chatui.internal.util.convertToString
import eu.brrm.chatui.internal.util.parseAsJSON
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

internal class NetworkClientImpl(private val chatUrl: String, private val storage: Storage) :
    NetworkClient {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun <T> execute(request: Request, adapter: Adapter<T>): Response<T> {
        val urlPath = buildUrl(request)
        val fullPath = "$chatUrl$urlPath"
        val url = URL(fullPath)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

        var response: Response<T>
        connection.requestMethod = request.method.methodName
        connection.doInput = request.doInput
        connection.doOutput = request.doOutput
        connection.readTimeout = TimeUnit.SECONDS.toMillis(15).toInt()
        connection.connectTimeout = TimeUnit.SECONDS.toMillis(15).toInt()

        try {
            request.headers.entries.forEach {
                it.value?.let { value ->
                    connection.setRequestProperty(it.key, value.toString())
                }
            }

            val data = request.requestBody?.toJson()?.toString()
            if (!data.isNullOrEmpty()) {
                DataOutputStream(connection.outputStream).run {
                    write(data.toByteArray(Charsets.UTF_8))
                    flush()
                    close()
                }
            }
            logDebug("REQUEST - ${request.method.name.uppercase()}: $fullPath\nRequestBody: $data \nHeaders: ${
                request.headers.map { "\"${it.key}\":\"${it.value}\"" }
                    .joinToString(separator = "\n")
            }")

            val statusCode = connection.responseCode
            logDebug("API STATUS CODE: $statusCode; URL: $fullPath")

            val result = connection.inputStream.convertToString()
            val error = connection.errorStream.convertToString()
            result?.let { logInfo("API RESULT: $it") }
            error?.let { logError("API ERROR: $it") }
            response = resolveResponse(adapter, statusCode, result, error)
        } catch (e: Exception) {
            response = adapter.createResponse(statusCode = 0, null, e)
        } finally {
            connection.disconnect()
        }

        return response
    }

    private fun <T> resolveResponse(
        adapter: Adapter<T>,
        statusCode: Int,
        result: String?,
        error: String?
    ): Response<T> {
        val response: Response<T>
        when (statusCode) {
            in 200..299 -> {
                val json = result.parseAsJSON()
                response = adapter.createResponse(statusCode, json)
            }

            in 300..399 -> {
                response = adapter.createResponse(
                    statusCode,
                    null,
                    Throwable("Redirect exception: $error")
                )
            }

            in 400..499 -> {
                response = adapter.createResponse(
                    statusCode,
                    null,
                    Throwable("Client exception: $error")
                )
            }

            in 500..599 -> {
                response = adapter.createResponse(
                    statusCode,
                    null,
                    Throwable("Server exception: $error")
                )
            }

            else -> {
                response =
                    adapter.createResponse(
                        statusCode,
                        null,
                        Throwable("Unknown network error: $error")
                    )
            }
        }
        return response
    }

    private fun buildUrl(request: Request): String {
        val queryPath = buildQueryPath(request = request)
        val sb = StringBuffer()
            .append(request.path)
            .append(queryPath)
        return sb.toString()
    }

    private fun buildQueryPath(request: Request): String {
        val queryPath = StringBuffer()

        request.queryParams.entries.mapIndexed { index, entry ->
            if (index == 0 && !request.path.contains("?")) {
                queryPath.append("?")
            }
            queryPath.append("${entry.key}=${entry.value}")
            if (index < request.queryParams.entries.size - 1) {
                queryPath.append("&")
            }
        }
        return queryPath.toString()
    }
}