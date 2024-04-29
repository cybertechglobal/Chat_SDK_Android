package eu.brrm.chatui.internal.network.data

import eu.brrm.chatui.internal.data.BrrmUser
import org.json.JSONObject

internal class RegisterResponse(val token: String?, val user: BrrmUser?) {

    companion object {
        fun fromJson(json: JSONObject?): RegisterResponse {
            val token = json?.optString("token")
            val user = json?.optJSONObject("user")?.let {
                BrrmUser.fromJSON(it)
            }
            return RegisterResponse(token, user)
        }

    }
}