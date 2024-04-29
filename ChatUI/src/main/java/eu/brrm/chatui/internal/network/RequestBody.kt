package eu.brrm.chatui.internal.network

import org.json.JSONObject

internal interface RequestBody {
    fun toJson(): JSONObject
}