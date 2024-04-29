package eu.brrm.chatui.internal.util

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun InputStream?.convertToString(): String? {
    val sb = StringBuilder()
    this?.let {
        BufferedReader(InputStreamReader(this)).run {
            var line: String?
            try {
                do {
                    line = readLine()
                    if (!line.isNullOrEmpty()) {
                        sb.append(line)
                    }
                } while (line != null)
            } catch (ignored: Exception) {
            }
        }
    }
    return if (sb.isEmpty()) null else sb.toString()
}

fun String?.parseAsJSON(): JSONObject {
    if (this.isNullOrEmpty()) return JSONObject()
    return try {
        JSONObject(this)
    } catch (e: Exception) {
        JSONObject().apply {
            put("data", this)
        }
    }
}