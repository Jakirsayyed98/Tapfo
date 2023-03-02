package app.tapho

import android.app.Application
import android.content.Context
import androidx.room.Room
import app.tapho.utils.AdBlocker
import net.one97.paytm.nativesdk.PaytmSDK


class TapfoApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: TapfoApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

    }


    override fun onCreate() {
        super.onCreate()
        PaytmSDK.init(this)
        val context: Context = TapfoApplication.applicationContext()
        AdBlocker.init(this)

    }


}