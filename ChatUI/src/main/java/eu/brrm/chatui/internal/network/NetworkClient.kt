package eu.brrm.chatui.internal.network

internal interface NetworkClient {
    suspend fun <T> execute(request: Request, adapter: Adapter<T>): Response<T>
}