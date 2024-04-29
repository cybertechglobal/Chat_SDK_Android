package eu.brrm.chatui.internal

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.ChatLog.logDebug
import eu.brrm.chatui.internal.bridge.Chat.APP_NAME
import eu.brrm.chatui.internal.bridge.Chat.KEY_APP_NAME
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.module.LibraryModule
import eu.brrm.chatui.internal.network.ChatApi
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

    private val chatApi: ChatApi

    init {
        logDebug("init()")
        LibraryModule.init(context)
        coroutineScope = LibraryModule.coroutineScope()
        deviceService = LibraryModule.deviceService()
        storage = LibraryModule.getStorage()
        chatApi = LibraryModule.chatApi()
        appToken?.let {
            coroutineScope.launch {
                storage.saveToken(it)
            }
        }
    }

    override fun register(
        brrmUser: BrrmUser,
        brrmGroup: BrrmGroup,
        fcmToken: String?,
        onRegister: ((Boolean) -> Unit)?
    ) {
        coroutineScope.launch {
            val response = chatApi.register(brrmUser, brrmGroup)
            val userToken = response.data?.token
            if (response.isSuccess() && userToken != null) {
                storage.saveUserAndGroup(brrmUser.copy(userToken = userToken), brrmGroup)
            }

            fcmToken?.let {
                subscribeDevice(it)
            }

            onRegister?.invoke(response.isSuccess())
        }
    }

    override fun subscribeDevice(token: String, onSubscribe: ((Boolean) -> Unit)?) {
        coroutineScope.launch {
            val response = chatApi.subscribe(token)
            if (response.isSuccess()) {
                deviceService.subscribeDevice(token)
            }
            onSubscribe?.invoke(response.isSuccess())
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

    override fun isBrrmChatMessage(message: Map<*, *>): Boolean {
        val bundle = Bundle().apply {
            message.entries.forEach { item ->
                putString(item.key.toString(), item.value.toString())
            }
        }
        val remoteMessage = RemoteMessage(bundle)
        return isBrrmChatMessage(remoteMessage)
    }

    override fun handleBrrmChatMessage(message: RemoteMessage) {
        deviceService.handleChatMessage(message)
    }

    override fun handleBrrmChatMessage(message: Map<*, *>) {
        val bundle = Bundle().apply {
            message.entries.forEach {
                this.putString(it.key.toString(), it.value.toString())
            }
        }
        val remoteMessage = RemoteMessage(bundle)
        handleBrrmChatMessage(remoteMessage)
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