package eu.brrm.chatui.internal.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.bridge.Chat
import eu.brrm.chatui.internal.bridge.ChatBroadcastReceiver
import eu.brrm.chatui.internal.bridge.NativeInterface
import eu.brrm.chatui.internal.bridge.NativeInterfaceImpl
import eu.brrm.chatui.internal.data.ChatMessage
import eu.brrm.chatui.internal.module.LibraryModule
import eu.brrm.chatui.internal.permission.PermissionManager
import eu.brrm.chatui.internal.storage.Storage
import org.json.JSONObject

internal class ChatListActivity : AppCompatActivity() {

    private val TAG = ChatListActivity::class.java.name

    private lateinit var nativeInterface: NativeInterface

    private lateinit var webView: BrrmWebView

    private lateinit var chatBroadcastReceiver: ChatBroadcastReceiver

    private lateinit var permissionManager: PermissionManager

    companion object {
        fun createIntent(context: Context, bundle: Bundle? = null): Intent =
            Intent(context, ChatListActivity::class.java).apply {
                setPackage(context.packageName)
                addFlags(
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_NO_HISTORY
                )
                bundle?.let { putExtras(it) }
            }
    }

    private val onBackHandler: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            nativeInterface.getCurrentScreen(webView = webView) {
                val screen = it.replace("\"", "")
                if (screen == Chat.CHAT_SCREEN_CHAT_LIST) {
                    finish()
                } else {
                    nativeInterface.openPage(webView, "chat-list")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter().apply {
            addAction(packageName)
        }
        ContextCompat.registerReceiver(
            this, chatBroadcastReceiver, intentFilter, ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "onStart() - BroadcastReceiver registered!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        onBackPressedDispatcher.addCallback(onBackHandler)

        permissionManager = PermissionManager(this@ChatListActivity)

        nativeInterface = NativeInterfaceImpl(
            coroutineScope = lifecycleScope,
            onCloseChat = { finish() }
        ).apply {
            intent?.extras?.getString(Chat.CHAT_MESSAGE_KEY)?.let {
                val chat = ChatMessage.fromJson(JSONObject(it))
                setChatId(chat.chatId)
            }
        }

        webView = BrrmWebView(this@ChatListActivity, nativeInterface, permissionManager).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        findViewById<FrameLayout>(R.id.root).also {
            it.addView(webView)
        }

        chatBroadcastReceiver = ChatBroadcastReceiver(webView, nativeInterface)
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        unregisterReceiver(chatBroadcastReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}