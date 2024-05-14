package eu.brrm.chatui.internal.network.data

import eu.brrm.chatui.internal.network.RequestBody
import org.json.JSONObject

internal class SubscribeDevice(private val uniqueId: String, private val fcmToken: String) :
    RequestBody {
    override fun toJson(): JSONObject {
        return JSONObject().apply {
            put("uniqueId", uniqueId)
            put("fcmToken", fcmToken)
        }
    }

    override fun toString(): String {
        return "SubscribeDevice(uniqueId='$uniqueId', fcmToken='$fcmToken')"
    }
}
