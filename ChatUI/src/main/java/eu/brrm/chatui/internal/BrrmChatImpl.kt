package eu.brrm.chatui.internal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.internal.bridge.Chat.APP_NAME
import eu.brrm.chatui.internal.bridge.Chat.KEY_APP_NAME
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.module.LibraryModule
import eu.brrm.chatui.internal.service.DeviceService
import eu.brrm.chatui.internal.storage.Storage
import eu.brrm.chatui.internal.ui.ChatListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class BrrmChatImpl internal constructor(
    context: Application,
    appToken: String? = null
) : BrrmChat {

    private val TAG = BrrmChatImpl::class.java.name

    private val deviceService: DeviceService

    private val coroutineScope: CoroutineScope

    private val storage: Storage

    init {
        Log.d(TAG, "init()")
        LibraryModule.init(context)
        coroutineScope = LibraryModule.coroutineScope()
        deviceService = LibraryModule.deviceService()
        storage = LibraryModule.getStorage()
        appToken?.let {
            coroutineScope.launch {
                storage.saveToken(it)
            }
        }
    }

    override fun openChatList(context: Context) {
        ChatListActivity.createIntent(context).let {
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
        coroutineScope.launch {
            storage.saveUser(brrmUser)
        }
    }

    override fun setGroup(brrmGroup: BrrmGroup) {
        coroutineScope.launch {
            storage.saveGroup(brrmGroup)
        }
    }

    override fun setChatIconDrawable(@DrawableRes icon: Int) {
        coroutineScope.launch {
            storage.saveIconDrawable(icon)
        }
    }

    override fun setChatIconColor(@ColorRes color: Int) {
        coroutineScope.launch {
            storage.saveIconColor(color)
        }
    }
}