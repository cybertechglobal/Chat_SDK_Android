package eu.brrm.chatui.internal.module

import android.app.Application
import android.util.Log
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.BrrmChatImpl
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.service.DeviceService
import eu.brrm.chatui.internal.service.DeviceServiceImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal object LibraryModule {
    private lateinit var context: Application
    private lateinit var appToken: String

    var brrmUser: BrrmUser? = null
    var brrmGroup: BrrmGroup? = null

    fun init(context: Application, appToken: String) {
        this.context = context
        this.appToken = appToken
    }

    private val coroutineScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(BrrmChatImpl::class.simpleName, "Exception in executing coroutine", throwable)
        })
    }

    private val deviceService: DeviceService by lazy {
        DeviceServiceImpl(context, coroutineScope)
    }

    fun deviceService(): DeviceService {
        return deviceService
    }

    fun coroutineScope(): CoroutineScope {
        return coroutineScope
    }

    fun getAppToken(): String {
        return appToken
    }
}