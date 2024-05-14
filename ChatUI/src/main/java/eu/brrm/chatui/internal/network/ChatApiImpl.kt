package eu.brrm.chatui.internal.network

import eu.brrm.chatui.ChatLog.logInfo
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.network.data.RegisterBody
import eu.brrm.chatui.internal.network.data.RegisterResponse
import eu.brrm.chatui.internal.network.data.RegisterUser
import eu.brrm.chatui.internal.network.data.SubscribeDevice
import eu.brrm.chatui.internal.network.data.SubscribeResponse
import eu.brrm.chatui.internal.service.DeviceService
import eu.brrm.chatui.internal.storage.Storage

internal class ChatApiImpl(
    private val networkClient: NetworkClient,
    private val storage: Storage,
    private val deviceService: DeviceService
) :
    ChatApi {

    override suspend fun register(
        user: BrrmUser,
        group: BrrmGroup,
    ): Response<RegisterResponse> {
        logInfo("Register")
        val appToken = storage.getToken()
            ?: throw IllegalArgumentException("SDK must be initialized and AppToken provided!")
        val registerUser = RegisterUser(
            user.email,
            user.name,
            user.id,
            group.name,
            group.id,
            user.isGroupAdmin,
            deviceService.getUniqueDeviceId()
        )
        val requestBody = RegisterBody(appToken, registerUser)
        val headers = mapOf("Content-Type" to "application/json")
        val request = Request.Post(path = "v1/appAuth", requestBody = requestBody, headers = headers)
        val adapter = BaseAdapterImpl { json ->
            return@BaseAdapterImpl RegisterResponse.fromJson(json)
        }
        return networkClient.execute(request, adapter)
    }

    override suspend fun subscribe(fcmToken: String): Response<SubscribeResponse> {
        val requestBody = SubscribeDevice(deviceService.getUniqueDeviceId(), fcmToken)
        val userToken = storage.getUser()?.userToken
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $userToken"
        )
        val request =
            Request.Post(path = "v1/devices", requestBody = requestBody, headers = headers)
        val adapter = BaseAdapterImpl {
            SubscribeResponse.fromJSon(it)
        }
        return networkClient.execute(request, adapter)
    }
}