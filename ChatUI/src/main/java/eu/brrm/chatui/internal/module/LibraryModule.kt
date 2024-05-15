package eu.brrm.chatui.internal.module

import android.app.Application
import android.util.Log
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.BrrmChatImpl
import eu.brrm.chatui.internal.ChatEnvironment
import eu.brrm.chatui.internal.network.ChatApi
import eu.brrm.chatui.internal.network.ChatApiImpl
import eu.brrm.chatui.internal.network.NetworkClient
import eu.brrm.chatui.internal.network.NetworkClientImpl
import eu.brrm.chatui.internal.service.DeviceService
import eu.brrm.chatui.internal.service.DeviceServiceImpl
import eu.brrm.chatui.internal.storage.PrefsStorageImpl
import eu.brrm.chatui.internal.storage.Storage
import eu.brrm.chatui.internal.ui.notification.NotificationFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal object LibraryModule {
    private lateinit var mContext: Application

    private lateinit var chatUrl: String
    fun init(context: Application, chatEnvironment: ChatEnvironment) {
        this.mContext = context
        this.chatUrl =
            if (chatEnvironment == ChatEnvironment.PRODUCTION)
                context.getString(R.string.CHAT_API_PROD)
            else
                context.getString(R.string.CHAT_API_DEV)
    }

    private val mCoroutineScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(BrrmChatImpl::class.simpleName, "Exception in executing coroutine", throwable)
        })
    }

    private val mStorage: Storage by lazy { PrefsStorageImpl(mContext) }

    private val mNotificationFactory: NotificationFactory by lazy {
        NotificationFactory(
            mContext,
            mStorage
        )
    }

    private val mDeviceService: DeviceService by lazy {
        DeviceServiceImpl(mContext, mCoroutineScope, mNotificationFactory)
    }

    private val networkClient: NetworkClient by lazy { NetworkClientImpl(chatUrl) }

    fun deviceService(): DeviceService {
        return mDeviceService
    }

    fun coroutineScope(): CoroutineScope {
        return mCoroutineScope
    }

    fun getStorage(): Storage {
        return mStorage
    }

    fun chatApi(): ChatApi {
        return ChatApiImpl(networkClient, getStorage(), deviceService())
    }
}