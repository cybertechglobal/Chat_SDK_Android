package eu.brrm.chatui.internal.service

import com.google.firebase.messaging.RemoteMessage

internal interface DeviceService {

    fun subscribeDevice(token: String)

    fun getUniqueDeviceId(): String

    fun handleChatMessage(remoteMessage: RemoteMessage)

    fun handleChatMessage(map: Map<*, *>)
}