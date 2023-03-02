package app.tapho.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import app.tapho.ui.ContainerActivity
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
            notification.setLargeIcon(
                Glide.with(this).asBitmap().load(message.data["icon1"]).submit().get()
            )
        }


        val intent = Intent(this.applicationContext, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        if (message.data["screen_url"].toString().isNullOrEmpty().not()){
            when(message.data["screen_url"].toString()){
                "com.tapfo.redirectScreen.Games"->{
                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("app_id", message.data["app_id"].toString())
                    inten.putExtra("tag", message.data["tag"].toString())
                    inten.putExtra("screen_url", message.data["screen_url"].toString())

                    val Pintent = PendingIntent.getActivities(applicationContext, Random.nextInt(), arrayOf(inten), PendingIntent.FLAG_IMMUTABLE)
                    notification.setContentIntent(Pintent)



//                    getSharedPreference().saveString("app_id", message.data["app_id"].toString())
//                    getSharedPreference().saveString("tag", message.data["tag"].toString())
//                    getSharedPreference().saveString("screen_url", message.data["screen_url"].toString())
                }
                "com.tapfo.ui.WebviewActivity"->{

                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("app_id", message.data["app_id"].toString())
                    inten.putExtra("tag", message.data["tag"].toString())
                    inten.putExtra("screen_url", message.data["screen_url"].toString())

                    val Pintent = PendingIntent.getActivities(applicationContext, Random.nextInt(), arrayOf(inten), PendingIntent.FLAG_IMMUTABLE)
                    notification.setContentIntent(Pintent)


//                    getSharedPreference().saveString("app_id", message.data["app_id"].toString())
//                    getSharedPreference().saveString("tag", message.data["tag"].toString())
//                    getSharedPreference().saveString("screen_url", message.data["screen_url"].toString())

                }
                "com.tapfo.ui.ContainerActivity"->{

                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("app_id", message.data["app_id"].toString())
                    inten.putExtra("tag", message.data["tag"].toString())
                    inten.putExtra("screen_url", message.data["screen_url"].toString())

                    val Pintent = PendingIntent.getActivities(applicationContext, Random.nextInt(), arrayOf(inten), PendingIntent.FLAG_IMMUTABLE)
                    notification.setContentIntent(Pintent)


//                    getSharedPreference().saveString("app_id", message.data["app_id"].toString())
//                    getSharedPreference().saveString("tag", message.data["tag"].toString())
//                    getSharedPreference().saveString("screen_url", message.data["screen_url"].toString())

                }
                "com.tapfo.ui.home.WebViewActivityForOffer"->{

                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("app_id", message.data["app_id"].toString())
                    inten.putExtra("tag", message.data["tag"].toString())
                    inten.putExtra("screen_url", message.data["screen_url"].toString())

                    val Pintent = PendingIntent.getActivities(applicationContext, Random.nextInt(), arrayOf(inten), PendingIntent.FLAG_IMMUTABLE)
                    notification.setContentIntent(Pintent)


//                    getSharedPreference().saveString("app_id", message.data["app_id"].toString())
//                    getSharedPreference().saveString("tag", message.data["tag"].toString())
//                    getSharedPreference().saveString("screen_url", message.data["screen_url"].toString())

                }
                else->{
                    val inten = Intent(this.applicationContext, SplashActivity::class.java)
                    inten.putExtra("app_id", message.data["app_id"].toString())
                    inten.putExtra("tag", message.data["tag"].toString())
                    inten.putExtra("screen_url", message.data["screen_url"].toString())

                    val Pintent = PendingIntent.getActivities(applicationContext, Random.nextInt(), arrayOf(inten), PendingIntent.FLAG_IMMUTABLE)
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

