package eu.brrm.chatui.internal.storage

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import kotlinx.coroutines.flow.first
import org.json.JSONObject

private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "BrrmChatUI")

class PrefsStorageImpl(context: Context) : Storage {
    private val dataStore: DataStore<Preferences> by lazy { (context.applicationContext as Application).dataStore }
    private val groupKey = stringPreferencesKey("brrmGroup")
    private val userKey = stringPreferencesKey("brrmUser")
    private val tokenKey = stringPreferencesKey("token")
    private val iconDrawableKey = intPreferencesKey("iconDrawableKey")
    private val iconColorKey = intPreferencesKey("iconColorKey")

    override suspend fun getGroup(): BrrmGroup? {
        val data = dataStore.data.first()[groupKey]
        return data?.let { BrrmGroup.fromJSON(JSONObject(it)) }
    }

    override suspend fun getUser(): BrrmUser? {
        val data = dataStore.data.first()[userKey]
        return data?.let { BrrmUser.fromJSON(JSONObject(it)) }
    }

    override suspend fun saveUserAndGroup(user: BrrmUser, group: BrrmGroup) {
        dataStore.edit {
            it[userKey] = user.toJSON().toString()
            it[groupKey] = group.toJSON().toString()
        }
    }

    override suspend fun saveIconDrawable(icon: Int) {
        dataStore.edit {
            it[iconDrawableKey] = icon
        }
    }

    override suspend fun getIconDrawable(): Int? {
        return dataStore.data.first()[iconDrawableKey]
    }

    override suspend fun saveIconColor(color: Int) {
        dataStore.edit {
            it[iconColorKey] = color
        }
    }

    override suspend fun getIconColor(): Int? {
        return dataStore.data.first()[iconColorKey]
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit {
            it[tokenKey] = token
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.first()[tokenKey]
    }
}