package eu.brrm.chattestapp

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val webView = WebView(applicationContext).apply {
            layoutParams =
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            loadUrl("https://testchat.brrm.eu/chat.html")
        }
        findViewById<FrameLayout>(R.id.root).also {
            it.addView(webView)
        }
    }

    override fun onDestroy() {
        try {
            webView?.webChromeClient = null
            webView?.clearHistory()
            webView?.clearCache(true)
            webView?.removeAllViews()
            webView?.destroy()
            webView = null
        } catch (ignored: Exception) {
        }
        super.onDestroy()
    }
}