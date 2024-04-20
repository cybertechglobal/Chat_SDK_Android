package eu.brrm.chatui.internal.data

import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

internal data class ChatMessage(
    val chatId: String,
    val appName: String,
    val eventName: String,
    val message: InternalMessage?,
    val recipientId: String
) {

    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("chatId", chatId)
            put("appName", appName)
            put("eventName", eventName)
            put("recipientId", recipientId)
            message?.toJson()?.let {
                put("message", it)
            }
        }
    }

    companion object {
        fun RemoteMessage.toChatMessage(): ChatMessage {
            val map = this.data
            return fromMap(map)
        }

        fun fromJson(json: JSONObject): ChatMessage {
            val chatId = json.optString("chatId") ?: ""
            val appName = json.optString("appName") ?: ""
            val eventName = json.optString("eventName") ?: ""
            val recipientId = json.optString("recipientId") ?: ""
            val message = json.optJSONObject("message")

            return ChatMessage(
                chatId,
                appName,
                eventName,
                message?.let { InternalMessage.fromJson(it) },
                recipientId
            )
        }

        private fun fromMap(map: Map<String, String>): ChatMessage {
            val chatId = map["chatId"] ?: ""
            val appName = map["appName"] ?: ""
            val eventName = map["eventName"] ?: ""
            val recipientId: String = map["recipientId"] ?: ""
            val message = map["message"]?.let {
                JSONObject(it).let { internalJson ->
                    InternalMessage.fromJson(internalJson)
                }
            }
            return ChatMessage(chatId, appName, eventName, message, recipientId)
        }
    }
}

internal data class InternalMessage(
    val id: String,
    val chatId: String,
    val message: String,
    val senderId: String,
) {

    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("chatId", chatId)
            put("message", message)
            put("senderId", senderId)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): InternalMessage {
            val id = json.optString("id")
            val chatId = json.optString("chatId")
            val message = json.optString("message")
            val senderId = json.optString("senderId")
            return InternalMessage(id, chatId, message, senderId)
        }
    }
}