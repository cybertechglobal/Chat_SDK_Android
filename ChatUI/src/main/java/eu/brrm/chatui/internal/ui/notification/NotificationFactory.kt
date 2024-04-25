@file:Suppress("PrivatePropertyName")

package eu.brrm.chatui.internal.ui.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import eu.brrm.chatui.R
import eu.brrm.chatui.internal.storage.Storage
import eu.brrm.chatui.internal.ui.ChatListActivity
import kotlin.random.Random

internal class NotificationFactory(
    private val context: Context,
    private val storage: Storage,
) {

    private val DEFAULT_NOTIFICATION_ICON = "eu.brrm.chatui.default_notification_icon"
    private val DEFAULT_NOTIFICATION_COLOR = "eu.brrm.chatui.default_notification_color"

    private val notificationChannelId = "${context.packageName}.chat"

    private val notificationManager = NotificationManagerCompat.from(context)

    private val random = Random(10000)
    suspend fun createNotification(title: String?, message: String?, bundle: Bundle): Notification {
        createNotificationChannel()

        val finalTitle = title ?: "New message"
        val finalMessage = message ?: "Click to open chat message"

        val notificationStyle =
            NotificationCompat.BigTextStyle().setSummaryText(message).setBigContentTitle(finalTitle)

        val defaultNotificationColor = getDefaultNotificationColor()

        val defaultNotificationIcon = getDefaultNotificationIcon()

        val icon = storage.getIconDrawable() ?: defaultNotificationIcon

        val color = storage.getIconColor()?.let { resolveColor(it) }
            ?: defaultNotificationColor

        val builder = NotificationCompat.Builder(context, notificationChannelId)
            .setStyle(notificationStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(finalTitle)
            .setContentText(finalMessage)
            .setSmallIcon(icon ?: R.drawable.ic_chat_bubble_icon)
            .setColor(color ?: android.R.color.white)
            .setAutoCancel(true)
            .setOngoing(false)

        val pendingIntent = createContentIntent(bundle)

        builder.setContentIntent(pendingIntent)

        return builder.build()
    }

    @SuppressLint("MissingPermission")
    fun show(notification: Notification) {
        val id = random.nextInt(100, 10000)
        notificationManager.notify(id, notification)
    }

    private fun getDefaultNotificationIcon(): Int? {
        val icon = getMetadata().getInt(DEFAULT_NOTIFICATION_ICON)
        return if (icon != 0) icon else null
    }

    private fun getDefaultNotificationColor(): Int? {
        val colorId = getMetadata().getInt(DEFAULT_NOTIFICATION_COLOR)
        return if (colorId != 0) ContextCompat.getColor(context, colorId) else null
    }

    private fun resolveColor(colorRes: Int?): Int? {
        if (colorRes == null) return null
        return try {
            ContextCompat.getColor(context, colorRes)
        } catch (e: Exception) {
            colorRes
        }
    }

    private fun getMetadata(): Bundle {
        return context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        ).metaData
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    notificationChannelId,
                    notificationChannelId,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    private fun createContentIntent(bundle: Bundle): PendingIntent {
        val intent = ChatListActivity.createIntent(context, bundle)
        val flags =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT else PendingIntent.FLAG_CANCEL_CURRENT
        return PendingIntent.getActivity(context, 1, intent, flags)
    }
}