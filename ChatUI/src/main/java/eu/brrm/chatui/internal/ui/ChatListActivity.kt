package eu.brrm.chatui.internal.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.bridge.Chat
import eu.brrm.chatui.internal.bridge.ChatBroadcastReceiver
import eu.brrm.chatui.internal.bridge.ChatChromeClient
import eu.brrm.chatui.internal.bridge.ChatWebClient
import eu.brrm.chatui.internal.bridge.JsInterface
import eu.brrm.chatui.internal.bridge.NativeInterface
import eu.brrm.chatui.internal.bridge.NativeInterfaceImpl
import eu.brrm.chatui.internal.permission.PermissionManager

internal class ChatListActivity : AppCompatActivity() {

    private val TAG = ChatListActivity::class.java.name

    private val nativeInterface: NativeInterface = NativeInterfaceImpl(
        coroutineScope = lifecycleScope,
        onCloseChat = { finish() }
    )

    private lateinit var webView: WebView

    private lateinit var jsInterface: JsInterface

    private lateinit var chatBroadcastReceiver: ChatBroadcastReceiver

    private lateinit var permissionManager: PermissionManager

    private val onBackHandler: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            nativeInterface.getCurrentScreen(webView = webView) {
                val screen = it.replace("\"", "")
                if (screen == Chat.CHAT_SCREEN_CHAT_LIST) {
                    finish()
                } else {
                    nativeInterface.openPage(webView, "chat-list")
                    this.isEnabled = false
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
        webView = WebView(applicationContext).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        findViewById<FrameLayout>(R.id.root).also {
            it.addView(webView)
        }

        permissionManager = PermissionManager(this)

        chatBroadcastReceiver = ChatBroadcastReceiver(webView, nativeInterface)

        jsInterface = JsInterface(webView, nativeInterface)

        setupWebView(webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(webView: WebView) {
        webView.apply {
            addJavascriptInterface(jsInterface, JsInterface.interfaceName)

            settings.apply {
                javaScriptEnabled = true
                cacheMode = WebSettings.LOAD_NO_CACHE
                textZoom = 100
                useWideViewPort = true
                loadWithOverviewMode = true
                domStorageEnabled = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                settings.safeBrowsingEnabled = false
            }

            webViewClient = ChatWebClient()

            webChromeClient =
                ChatChromeClient(permissionManager, callback = object : ChatChromeClient.Callback {
                    override fun onPermissionCanceled(message: String) {
                        Toast.makeText(this@ChatListActivity, message, Toast.LENGTH_SHORT).show()
                    }
                })

            val url = "${getString(R.string.BRRM_CHAT_BASE_DEV_URL)}?platform=android"
            loadUrl(url)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionManager.requestPermissions(listOf(Manifest.permission.POST_NOTIFICATIONS))
            }
        }
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        unregisterReceiver(chatBroadcastReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        webView.loadUrl("about:blank")
        webView.destroy()
        super.onDestroy()
    }
}