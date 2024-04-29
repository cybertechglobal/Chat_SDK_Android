package eu.brrm.chatui

import android.app.Application
import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.internal.BrrmChatImpl
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser

interface BrrmChat {
    fun register(
        brrmUser: BrrmUser,
        brrmGroup: BrrmGroup,
        fcmToken: String? = null,
        onRegister: ((Boolean) -> Unit)? = null
    )

    fun subscribeDevice(token: String, onSubscribe: ((Boolean) -> Unit)? = null)

    fun openChatList(context: Context)

    fun isBrrmChatMessage(message: RemoteMessage): Boolean

    fun isBrrmChatMessage(message: Map<*, *>): Boolean

    fun handleBrrmChatMessage(message: RemoteMessage)

    fun handleBrrmChatMessage(message: Map<*, *>)

    fun setChatIconDrawable(@DrawableRes icon: Int)

    fun setChatIconColor(@ColorRes color: Int)

    companion object {
        private lateinit var _instance: BrrmChat

        @JvmStatic
        val instance: BrrmChat
            get() = _instance

        @JvmStatic
        @JvmName("init")
        fun init(context: Context, appToken: String) {
            if (!this::_instance.isInitialized) {
                _instance = BrrmChatImpl(context.applicationContext as Application, appToken)
            }
        }
    }
}