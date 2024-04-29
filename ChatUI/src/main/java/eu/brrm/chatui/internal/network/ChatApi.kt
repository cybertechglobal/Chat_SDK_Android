package eu.brrm.chatui.internal.network

import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.network.data.RegisterResponse
import eu.brrm.chatui.internal.network.data.SubscribeResponse

internal interface ChatApi {
    suspend fun register(user: BrrmUser, group: BrrmGroup): Response<RegisterResponse>

    suspend fun subscribe(fcmToken: String): Response<SubscribeResponse>
}