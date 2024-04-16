package eu.brrm.chattestapp

import android.app.Application
import eu.brrm.chatui.BrrmChat

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BrrmChat.init(this,BuildConfig.CHAT_APP_TOKEN)
    }
}