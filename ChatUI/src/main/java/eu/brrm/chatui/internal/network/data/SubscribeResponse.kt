package eu.brrm.chatui.internal.network.data

import org.json.JSONObject

class SubscribeResponse(
    private val id: String?,
    private val userId: String?,
    private val uniqueId: String?,
    private val fcmToken: String?
) {
    companion object {
        fun fromJSon(json: JSONObject?): SubscribeResponse {
            val id = json?.optString("id")
            val userId = json?.optString("userId")
            val uniqueId = json?.optString("uniqueId")
            val fcmToken = json?.optString("fcmToken")

            return SubscribeResponse(id, userId, uniqueId, fcmToken)
        }
    }

    override fun toString(): String {
        return "SubscribeResponse(id=$id, userId=$userId, uniqueId=$uniqueId, fcmToken=$fcmToken)"
    }
}