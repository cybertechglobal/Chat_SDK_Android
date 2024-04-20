package eu.brrm.chatui.internal.storage

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser

interface Storage {
    suspend fun saveGroup(group: BrrmGroup)

    suspend fun getGroup(): BrrmGroup?

    suspend fun saveUser(user: BrrmUser)

    suspend fun getUser(): BrrmUser?

    suspend fun saveIconDrawable(@DrawableRes icon: Int)

    suspend fun getIconDrawable(): Int?
    suspend fun saveIconColor(@ColorRes color: Int)

    suspend fun getIconColor(): Int?

    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
}