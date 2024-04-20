package eu.brrm.chatui.internal.module

import android.app.Application
import android.util.Log
import eu.brrm.chatui.internal.BrrmChatImpl
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
    private lateinit var mAppToken: String

    fun init(context: Application, appToken: String) {
        this.mContext = context
        this.mAppToken = appToken
    }

    private val mCoroutineScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(BrrmChatImpl::class.simpleName, "Exception in executing coroutine", throwable)
        })
    }

    private val mStorage: Storage by lazy { PrefsStorageImpl(mContext) }

    private val mNotificationFactory: NotificationFactory
        get() = NotificationFactory(mContext, mStorage)

    private val mDeviceService: DeviceService by lazy {
        DeviceServiceImpl(mContext, mCoroutineScope, mNotificationFactory)
    }

    fun deviceService(): DeviceService {
        return mDeviceService
    }

    fun coroutineScope(): CoroutineScope {
        return mCoroutineScope
    }

    fun getStorage(): Storage {
        return mStorage
    }

    fun getAppToken(): String {
        return mAppToken
    }
}