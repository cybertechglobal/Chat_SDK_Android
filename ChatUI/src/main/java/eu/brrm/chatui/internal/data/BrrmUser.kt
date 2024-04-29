package eu.brrm.chatui.internal.data

import org.json.JSONObject

data class BrrmUser(
    val id: String,
    val email: String,
    val name: String,
    val isGroupAdmin: Boolean = false,
    val userToken: String? = null
) {
    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("email", email)
            put("name", name)
            put("isGroupAdmin", isGroupAdmin)
            put("userToken", userToken)
        }
    }

    companion object {
        fun fromJSON(json: JSONObject): BrrmUser {
            val id = json.optString("id")
            val email = json.optString("email")
            val name = json.optString("name")
            val isGroupAdmin = json.optBoolean("isGroupAdmin")
            val userToken = json.optString("userToken")
            return BrrmUser(id, email, name, isGroupAdmin, userToken)
        }
    }
}
