package eu.brrm.chatui.internal.bridge

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal object Chat {
    private val TAG: String = Chat::class.java.name

    const val EVENT_MESSAGE_NEW = "chat.message"
    const val EVENT_MESSAGE_SEEN = "message.seen"

    const val KEY_EVENT_NAME = "eventName"
    const val KEY_MESSAGE = "message"

    const val KEY_CHAT_ID = "chatId"
    const val KEY_APP_NAME = "appName"
    const val KEY_MESSAGE_ID = "messageId"
    const val APP_NAME = "BrrmChatApp"

    const val CHAT_SCREEN_SELECTED_CHAT = "selected_chat"
    const val CHAT_SCREEN_CHAT_LIST = "chat_list"

    suspend fun notifyChatUpdated(context: Context, remoteMessage: RemoteMessage) {
        val eventName = remoteMessage.data[KEY_EVENT_NAME]

        if (EVENT_MESSAGE_NEW == eventName || EVENT_MESSAGE_SEEN == eventName) {
            val chatId = remoteMessage.data[KEY_CHAT_ID]
            val messageId = remoteMessage.data[KEY_MESSAGE_ID]
            chatId?.let {
                withContext(Dispatchers.Main) {
                    context.sendBroadcast(Intent(context.packageName).apply {
                        putExtra(KEY_CHAT_ID, it)
                        putExtra(KEY_EVENT_NAME, eventName)
                        putExtra(KEY_MESSAGE_ID, messageId)
                    })
                    Log.d(TAG, "Broadcast sent from package: ${context.packageName}")
                }
            }
        }
    }
}