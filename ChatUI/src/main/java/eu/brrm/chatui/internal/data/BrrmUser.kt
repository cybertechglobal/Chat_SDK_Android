package eu.brrm.chatui.internal.data

import org.json.JSONObject

data class BrrmUser(val id: String, val email: String, val name: String) {
    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("email", email)
            put("name", name)
        }
    }

    companion object {
        fun fromJSON(json: JSONObject): BrrmUser {
            val id = json.optString("id")
            val email = json.optString("email")
            val name = json.optString("name")
            return BrrmUser(id, email, name)
        }
    }
}
