package eu.brrm.chattestapp

import android.app.Application
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.internal.ChatEnvironment

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BrrmChat.init(this, BuildConfig.CHAT_APP_TOKEN, ChatEnvironment.PRODUCTION)
        BrrmChat.instance.apply {
            setChatIconColor(R.color.ic_chat_icon_color)
            setChatIconDrawable(R.drawable.ic_chat_bubble_icon)
        }
    }
}