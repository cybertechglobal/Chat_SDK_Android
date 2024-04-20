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
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.bridge.ChatChromeClient
import eu.brrm.chatui.internal.bridge.ChatWebClient
import eu.brrm.chatui.internal.bridge.JsInterface
import eu.brrm.chatui.internal.bridge.NativeInterface
import eu.brrm.chatui.internal.permission.PermissionManager

@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
internal class BrrmWebView(
    activity: FragmentActivity,
    private val nativeInterface: NativeInterface,
    private val permissionManager: PermissionManager
) : WebView(activity.applicationContext), LifecycleEventObserver {

    private val jsInterface: JsInterface

    private val lifecycleOwner: LifecycleOwner = activity

    init {
        jsInterface = JsInterface(this, nativeInterface)
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
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })

        val url = "${context.getString(R.string.BRRM_CHAT_BASE_URL)}?platform=android"
        loadUrl(url)

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