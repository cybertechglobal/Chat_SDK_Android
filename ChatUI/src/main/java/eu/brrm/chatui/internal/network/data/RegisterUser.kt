package eu.brrm.chatui.internal.network.data

import eu.brrm.chatui.internal.network.RequestBody
import org.json.JSONObject

internal class RegisterUser(
    private val email: String,
    private val name: String,
    private val uniqueId: String,
    private val groupName: String,
    private val groupUniqueId: String,
    private val isGroupAdmin: Boolean,
    private val deviceUniqueId: String
) : RequestBody {
    override fun toJson(): JSONObject {
        return JSONObject().apply {
            put("email", email)
            put("name", name)
            put("uniqueId", uniqueId)
            put("groupName", groupName)
            put("groupUniqueId", groupUniqueId)
            put("isGroupAdmin", isGroupAdmin)
            put("deviceUniqueId", deviceUniqueId)
        }
    }
}