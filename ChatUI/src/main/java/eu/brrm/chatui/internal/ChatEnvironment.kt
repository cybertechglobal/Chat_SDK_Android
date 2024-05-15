package eu.brrm.chatui.internal

import androidx.annotation.Keep

@Keep
enum class ChatEnvironment(val value: Int) {
    PRODUCTION(value = 0),
    DEVELOPMENT(value = 1)
}