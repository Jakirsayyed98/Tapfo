package app.tapho.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.app.NotificationCompat
import app.tapho.ui.SplashActivity
import app.tapho.utils.AppSharedPreference
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class FCMNotification : FirebaseMessagingService() {
    private val channelID = "BitinfozCoder"
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/all")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createnotificationMagnager(manager as NotificationManager)

        val notification = NotificationCompat.Builder(this, channelID)
            .setContentText(message.data["body"])
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.data["body"]))
            .setSmallIcon(app.tapho.R.drawable.app_icon)
            .setAutoCancel(true)

        Log.d("notification", message.data.toString())

        if (message.data["custom_title"].isNullOrEmpty().not()) {
            notification.setContentTitle(message.data["custom_title"] + " " + message.data["title"])
        } else {
            notification.setContentTitle(message.data["title"])
        }

        kotlin.runCatching {
            notification.setStyle(NotificationCompat.BigPictureStyle().bigPicture(Glide.with(this).asBitmap().load(message.data["icon"]).submit().get()))
            notification.setLargeIcon(Glide.with(this).asBitmap().load(message.data["icon1"]).submit().get())


        }

        if (message.data.containsKey("screen_url")) {
            val inten = Intent(this.applicationContext, SplashActivity::class.java)
            inten.putExtra("TAGS", message.data["gamezop_id"].toString())
            inten.putExtra("type", "2")
            val Pintent = PendingIntent.getActivities(
                applicationContext,
                Random.nextInt(),
                arrayOf(inten),
                PendingIntent.FLAG_IMMUTABLE
            )
            notification.setContentIntent(Pintent)
        } else {
            when (message.data["tag"]) {
                "com.tapfo.ui.ContainerActivity" -> {
                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("TAGS", message.data["tag"].toString())
                    inten.putExtra("type", "1")
                    val Pintent = PendingIntent.getActivities(
                        applicationContext,
                        Random.nextInt(),
                        arrayOf(inten),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    notification.setContentIntent(Pintent)
                }
                "com.tapfo.ui.WebViewActivity" -> {
                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("TAGS", message.data["app_category_mini_app_id"].toString())
                    inten.putExtra("type", "3")
                    val Pintent = PendingIntent.getActivities(
                        applicationContext,
                        Random.nextInt(),
                        arrayOf(inten),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    notification.setContentIntent(Pintent)
                }
                "com.tapfo.ui.home.WebViewActivityForOffer" -> {
                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("TAGS", message.data["app_category_mini_app_id"].toString())
                    inten.putExtra("type", "4")
                    val Pintent = PendingIntent.getActivities(
                        applicationContext,
                        Random.nextInt(),
                        arrayOf(inten),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    notification.setContentIntent(Pintent)
                }
                else -> {
                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("TAGS", " ")
                    inten.putExtra("type", "5")
                    val Pintent = PendingIntent.getActivities(
                        applicationContext,
                        Random.nextInt(),
                        arrayOf(inten),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    notification.setContentIntent(Pintent)
                }
            }
        }


        manager.notify(Random.nextInt(), notification.build())
    }

    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(this)
    }

    fun createnotificationMagnager(manager: NotificationManager) {
        val channel =
            NotificationChannel(channelID, "BitinfozCoder", NotificationManager.IMPORTANCE_HIGH)

        channel.description = "Tapfo App"
        channel.enableLights(true)
        manager.createNotificationChannel(channel)
    }

}

