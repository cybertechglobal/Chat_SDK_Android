package eu.brrm.chatui.internal.bridge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import java.util.Objects

internal class ChatBroadcastReceiver(
    private var webView: WebView,
    private var nativeInterface: NativeInterface,
) : BroadcastReceiver() {
    private val TAG = ChatBroadcastReceiver::class.java.name

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: ${intent?.action}")
        if (Objects.equals(context?.packageName, intent?.action)) {
            val chatId = "${intent?.getStringExtra(Chat.KEY_CHAT_ID)}"
            val eventName = "${intent?.getStringExtra(Chat.KEY_EVENT_NAME)}"
            val messageId = "${intent?.getStringExtra(Chat.KEY_MESSAGE_ID)}"
            if (Chat.EVENT_MESSAGE_NEW == eventName) {
                val params = listOf<String>(chatId)
                nativeInterface.notify(webView, params)
            } else if (Chat.EVENT_MESSAGE_SEEN == eventName) {
                val params = listOf<String>(chatId, messageId)
                nativeInterface.notify(webView, params)
            }
        }
    }
}