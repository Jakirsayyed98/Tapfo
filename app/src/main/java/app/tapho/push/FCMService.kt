package app.tapho.push


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.RechargeApiNetwork.RechargeViewModel
import app.tapho.RechargesNetwork.RechargesRequestViewModel
import app.tapho.interfaces.ApiListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.WebViewActivity
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.one97.paytm.nativesdk.common.model.RiskExtendedInfoHolder.context


private const val CHANNEL_ID = "100210"

class FCMService : FirebaseMessagingService() {


    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/all")

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        AppSharedPreference.getInstance(applicationContext).saveString(FCM_TOKEN, p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        createNotification(p0.data,p0)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(map: Map<String, String>,p0: RemoteMessage) {
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
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        kotlin.runCatching {
            builder.setLargeIcon(Glide.with(this).asBitmap().load(map["icon"]).submit().get())
            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(Glide.with(this).asBitmap().load(map["icon"]).submit().get()))
        }
        val intent = Intent(map["url"]).apply {
            if (map["url"]=="com.tapfo.ui.ContainerActivity"){
                putExtra("container_type", map["tag"])
            }else if (map["url"]=="com.tapfo.redirectScreen.Games"){
                putExtra(WEB_VIEW_URL, map["mini_App_URL"])
                putExtra(Game_id, map["app_category_mini_app_id"])
            }else if (map["url"]=="com.tapfo.ui.WebViewActivity"){
                putExtra(MINI_APP_ID, map["app_category_mini_app_id"])
                putExtra(WEB_VIEW_URL,map["mini_App_URL"])
                putExtra(TCASH_TYPE, map["tcash"])
            }else if (map["url"]=="com.tapfo.ui.home.WebViewActivityForOffer"){
                putExtra(MINI_APP_ID,"81")// map["app_category_mini_app_id"])
                putExtra(WEB_VIEW_URL,"https://in.bookmyshow.com/")// map["mini_App_URL"])
                putExtra(TCASH_TYPE, map["tcash"])
            }
              flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pi = PendingIntent.getActivity(this, 120, intent, PendingIntent.FLAG_ONE_SHOT)
        builder.setContentIntent(pi)
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(mNotificationManager)
        mNotificationManager.notify(0, builder.build())
    }

    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(context!!)
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