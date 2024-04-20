package eu.brrm.chatui.internal.data

import org.json.JSONObject

data class BrrmGroup(val id: String, val name: String) {
    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("name", name)
        }
    }

    companion object {
        fun fromJSON(json: JSONObject): BrrmGroup {
            val id = json.optString("id")
            val name = json.optString("name")
            return BrrmGroup(id, name)
        }
    }
}
