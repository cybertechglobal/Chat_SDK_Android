package eu.brrm.chatui.internal.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.BrrmChat

internal class ChatPushService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        BrrmChat.instance.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.w(ChatPushService::class.java.name, "Message received in Library service")
        if(BrrmChat.instance.isBrrmChatMessage(message)) {
            BrrmChat.instance.handleBrrmChatMessage(message)
        }
    }
}