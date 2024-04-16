package eu.brrm.chatui.internal

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.internal.bridge.Chat.APP_NAME
import eu.brrm.chatui.internal.bridge.Chat.KEY_APP_NAME
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.module.LibraryModule
import eu.brrm.chatui.internal.service.DeviceService
import eu.brrm.chatui.internal.ui.ChatListActivity
import kotlinx.coroutines.CoroutineScope

internal class BrrmChatImpl internal constructor(
    context: Application,
    appToken: String
) : BrrmChat {

    private val TAG = BrrmChatImpl::class.java.name

    private val deviceService: DeviceService

    private val coroutineScope: CoroutineScope

    init {
        Log.d(TAG, "init()")
        LibraryModule.init(context, appToken)
        deviceService = LibraryModule.deviceService()
        coroutineScope = LibraryModule.coroutineScope()
        WebView(context.applicationContext)
    }

    override fun openChatList(context: Context) {
        Intent(context, ChatListActivity::class.java).apply {
            flags =
                flags or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        }.let {
            context.startActivity(it)
        }
    }

    override fun isBrrmChatMessage(message: RemoteMessage): Boolean {
        return message.data.containsKey(KEY_APP_NAME) && message.data[KEY_APP_NAME].equals(APP_NAME)
    }

    override fun onNewToken(token: String) {
        deviceService.subscribeDevice(token)
    }

    override fun handleBrrmChatMessage(message: RemoteMessage) {
        deviceService.handleChatMessage(message)
    }

    override fun setUser(brrmUser: BrrmUser) {
        LibraryModule.brrmUser = brrmUser
    }

    override fun setGroup(brrmGroup: BrrmGroup) {
        LibraryModule.brrmGroup = brrmGroup
    }
}