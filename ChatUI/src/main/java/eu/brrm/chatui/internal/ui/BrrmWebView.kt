package eu.brrm.chatui.internal.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.ChatEnvironment
import eu.brrm.chatui.internal.bridge.ChatChromeClient
import eu.brrm.chatui.internal.bridge.ChatWebClient
import eu.brrm.chatui.internal.bridge.JsInterface
import eu.brrm.chatui.internal.bridge.NativeInterface
import eu.brrm.chatui.internal.module.LibraryModule
import eu.brrm.chatui.internal.permission.PermissionManager
import eu.brrm.chatui.internal.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
internal class BrrmWebView(
    activity: FragmentActivity,
    nativeInterface: NativeInterface,
    permissionManager: PermissionManager,
) : WebView(activity.applicationContext), LifecycleEventObserver {

    private val jsInterface: JsInterface = JsInterface(this, nativeInterface)

    private val lifecycleOwner: LifecycleOwner = activity

    private val storage: Storage

    init {
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

        storage = LibraryModule.getStorage()

        webViewClient = ChatWebClient()

        webChromeClient =
            ChatChromeClient(permissionManager, callback = object : ChatChromeClient.Callback {
                override fun onPermissionCanceled(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })

        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val chatEnvironment = storage.getEnvironment()
            val widgetUrl = if (chatEnvironment == ChatEnvironment.PRODUCTION)
                context.getString(R.string.CHAT_WIDGET_URL_PROD)
            else
                context.getString(R.string.CHAT_WIDGET_URL_DEV)
            val url = "$widgetUrl?platform=android"
            withContext(Dispatchers.Main) {
                loadUrl(url)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.requestPermissions(listOf(Manifest.permission.POST_NOTIFICATIONS))
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                destroy()
                lifecycleOwner.lifecycle.removeObserver(this@BrrmWebView)
            }

            Lifecycle.Event.ON_CREATE -> {
                lifecycleOwner.lifecycle.addObserver(this@BrrmWebView)
            }

            else -> {}
        }
    }

    override fun destroy() {
        this.loadUrl("about:blank")
        super.destroy()
    }
}