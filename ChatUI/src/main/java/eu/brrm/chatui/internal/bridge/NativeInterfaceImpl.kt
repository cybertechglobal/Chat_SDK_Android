package eu.brrm.chatui.internal.bridge

import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import com.google.firebase.messaging.FirebaseMessaging
import eu.brrm.chatui.internal.module.LibraryModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject

class NativeInterfaceImpl(
    private val coroutineScope: CoroutineScope?,
    private val onCloseChat: () -> Unit
) :
    NativeInterface {
    private val TAG = NativeInterfaceImpl::class.java.name

    private var chatId: String? = null

    private val storage = LibraryModule.getStorage()

    private val deviceService = LibraryModule.deviceService()
    override fun setChatId(chatId: String?) {
        this.chatId = chatId
    }

    override fun auth(webView: WebView?) {
        coroutineScope?.launch {
            val appToken = storage.getToken()
            val user = storage.getUser()
                ?: throw NullPointerException("User must be initialized!")
            val group = storage.getGroup()
                ?: throw NullPointerException("Group must be initialized!")

            val json = JSONObject().apply {
                put("appAccessToken", appToken)
                put("uniqueId", user.id)
                put("email", user.email)
                put("name", user.name)
                put("groupUniqueId", group.id)
                put("groupName", group.name)
                put("deviceUniqueId", deviceService.getUniqueDeviceId())
                put("isGroupAdmin", user.isGroupAdmin)
                chatId?.let { put("chatId", it) }
            }.toString()
            chatId = null
            withContext(Dispatchers.Main) {
                executeJs(webView = webView, "auth", json)
            }
        }
    }

    override fun subscribeDevice(webView: WebView?) {
        coroutineScope?.launch {
            val token = FirebaseMessaging.getInstance().token.await()
            val json = JSONObject().apply {
                put("deviceUniqueId", LibraryModule.deviceService().getUniqueDeviceId())
                put("fcmToken", token)
            }.toString()
            withContext(Dispatchers.Main) {
                executeJs(webView = webView, commandName = "subscribe-device", json)
            }
        }
    }

    override fun notify(webView: WebView?, params: List<String>) {
        executeJs(webView = webView, commandName = "notify", params.joinToString { "," })
    }


    override fun getCurrentScreen(webView: WebView?, resultCallback: ValueCallback<String>?) {
        executeJs(
            webView = webView,
            commandName = "get-current-screen",
            params = null,
            resultCallback = resultCallback
        )
    }

    override fun openPage(webView: WebView?, pageName: String) {
        val json = JSONObject().apply {
            put("pageName", pageName)
        }.toString()
        executeJs(webView = webView, commandName = "open-page", params = json)
    }

    override fun closeChat() {
        onCloseChat.invoke()
    }

    private fun executeJs(
        webView: WebView?,
        commandName: String,
        params: String?,
        resultCallback: ValueCallback<String>? = null
    ) {
        val paramsBlock = if (!params.isNullOrEmpty()) (", $params") else ""
        val data = "\"${commandName}\"$paramsBlock"
        val command = "javascript:BrrmChat(${data})" //"javascript:BrrmChat("chatId","messageId")"
        webView?.evaluateJavascript(command, resultCallback)
        Log.w(TAG, "COMMAND ${commandName.uppercase()}: $command")
    }
}