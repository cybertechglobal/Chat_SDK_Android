package eu.brrm.chatui.internal.bridge

import android.util.Log
import android.util.LogPrinter
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject

internal class JsInterface(
    private var webView: WebView,
    private var nativeInterface: NativeInterface
) {

    companion object {
        @JvmStatic
        val interfaceName = "mobileClient"
    }

    private val TAG = JsInterface::class.java.name

    @JavascriptInterface
    fun onAuthFinished(json: String?) {
        Log.d(TAG, "onAuthFinished() - ${json?.let { JSONObject(it).toString() }}")
        nativeInterface.subscribeDevice(webView)
    }

    @JavascriptInterface
    fun onFinishedLoading() {
        Log.d(TAG, "onFinishedLoading()")
        nativeInterface.auth(webView)
    }

    @JavascriptInterface
    fun onDeviceSubscribed() {
        Log.d(TAG, "onDeviceSubscribed()")
    }

    @JavascriptInterface
    fun onCloseChat(){
        Log.d(TAG, "onCloseChat()")
        nativeInterface.closeChat()
    }
}