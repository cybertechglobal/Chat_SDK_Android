package eu.brrm.chatui.internal.bridge

import android.webkit.ValueCallback
import android.webkit.WebView

internal interface NativeInterface {
    fun setChatId(chatId:String?)

    fun auth(webView: WebView?)

    fun subscribeDevice(webView: WebView?)

    fun notify(webView: WebView?, params: List<String>)

    fun getCurrentScreen(webView: WebView?, resultCallback: ValueCallback<String>?)

    fun openPage(webView: WebView?, pageName:String)

    fun closeChat()
}