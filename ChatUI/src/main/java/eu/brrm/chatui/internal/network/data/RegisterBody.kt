package eu.brrm.chatui.internal.network.data

import eu.brrm.chatui.internal.network.RequestBody
import org.json.JSONObject

internal class RegisterBody(private val appAccessToken: String, private val user: RegisterUser) :
    RequestBody {
    override fun toJson(): JSONObject {
        return JSONObject().apply {
            put("appAccessToken", appAccessToken)
            put("user", user.toJson())
        }
    }
}