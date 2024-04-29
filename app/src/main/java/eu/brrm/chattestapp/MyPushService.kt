package eu.brrm.chattestapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.BrrmChat

class MyPushService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        BrrmChat.instance.subscribeDevice(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.w(MyPushService::class.java.name, "Message received in App service")
        super.onMessageReceived(message)
        if (BrrmChat.instance.isBrrmChatMessage(message)) {
            BrrmChat.instance.handleBrrmChatMessage(message)
        }else{
            // handle app messages
        }
    }
}