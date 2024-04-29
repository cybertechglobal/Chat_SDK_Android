package eu.brrm.chatui

import android.util.Log

object ChatLog {
    fun Any?.logDebug(message: String?) {
        val tag = this?.let { it::class.java.simpleName } ?: "ChatUI"
        Log.d(tag, message ?: "")
    }

    fun Any?.logError(message: String?) {
        val tag = this?.let { it::class.java.simpleName } ?: "ChatUI"
        Log.e(tag, message ?: "")
    }

    fun Any?.logError(e: Exception?) {
        val tag = this?.let { it::class.java.simpleName } ?: "ChatUI"
        Log.e(tag, e?.message ?: "Unknown error")
    }

    fun Any?.logInfo(message: String?) {
        val tag = this?.let { it::class.java.simpleName } ?: "ChatUI"
        Log.i(tag, message ?: "")
    }

    fun Any?.logWarn(message: String?) {
        val tag = this?.let { it::class.java.simpleName } ?: "ChatUI"
        Log.w(tag, message ?: "")
    }
}