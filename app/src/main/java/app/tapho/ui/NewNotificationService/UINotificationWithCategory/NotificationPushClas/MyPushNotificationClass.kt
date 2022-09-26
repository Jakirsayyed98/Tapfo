package app.tapho.ui.NewNotificationService.UINotificationWithCategory.NotificationPushClas

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import app.tapho.R

import app.tapho.ui.tcash.TCashRedeemActivity
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.ContainerType
import app.tapho.utils.FCM_TOKEN
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val CHANNEL_ID = "100210"

class MyPushNotificationClass  : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        AppSharedPreference.getInstance(applicationContext).saveString(FCM_TOKEN, p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        createNotification(p0.data)
        println("CHANNEL_ID $CHANNEL_ID " + p0.notification?.title)
        println("CHANNEL_IDada $CHANNEL_ID " + p0.data)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(map: Map<String, String>) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(map["title"])
            .setContentText(map["body"])
            .setStyle(NotificationCompat.BigTextStyle().bigText(map["body"]))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setAllowSystemGeneratedContextualActions(true)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        kotlin.runCatching {
            builder.setLargeIcon(Glide.with(this).asBitmap().load(map["image"]).submit().get())
        }
        val intent = Intent(this, TCashRedeemActivity::class.java).apply {
            putExtra(ContainerType.NOTIFICATIONDATA.name, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pi = PendingIntent.getActivity(this, 120, intent, PendingIntent.FLAG_ONE_SHOT)
        builder.setContentIntent(pi)
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(mNotificationManager)
        mNotificationManager.notify(0, builder.build())
    }

    private fun createNotificationChannel(mNotificationManager: NotificationManager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.channel_description)
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH).apply {
                description = descriptionText
                setShowBadge(true)
                lockscreenVisibility
            }
            // Register the channel with the system
            mNotificationManager.createNotificationChannel(channel)
        }
    }


}