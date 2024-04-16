package eu.brrm.chatui.internal.service

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import eu.brrm.chatui.internal.bridge.Chat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class DeviceServiceImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) :
    DeviceService {
    private val TAG = DeviceServiceImpl::class.simpleName

    override fun subscribeDevice(token: String) {
        coroutineScope.launch {
            Log.w(TAG, "FIREBASE TOKEN: $token")
        }
    }

    @Suppress("UnnecessaryVariable")
    @SuppressLint("HardwareIds")
    override fun getUniqueDeviceId(): String {
        val deviceId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.w(TAG, "UNIQUE DEVICE ID: $deviceId")
        return deviceId
    }

    override fun handleChatMessage(remoteMessage: RemoteMessage) {
        coroutineScope.launch {
            printMessage(remoteMessage)
            Chat.notifyChatUpdated(context, remoteMessage)
        }
    }

    private fun printMessage(message: RemoteMessage) {
        val data = message.data
        val notification = message.notification
        val messageId = message.messageId
        val sb = StringBuilder()

        sb.append("\n").append("==========================================")
        sb.append("\n").append("                 PUSH MESSAGE             ")
        sb.append("\n").append("==========================================")
        sb.append("\n").append("MessageId:").append(messageId).append("\n").append("\"data\":{")
            .append("\n")

        data.onEachIndexed { index, (key, value) ->
            sb.append("\"").append(key).append("\"").append(":").append("\"").append(value)
                .append("\"")
            if (index < data.size - 1) {
                sb.append(",")
            }
            sb.append("\n")
        }
        sb.append("}")
        sb.append("\n").append("==========================================")

        Log.d(TAG, sb.toString())
    }
}