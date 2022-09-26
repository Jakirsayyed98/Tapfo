package app.tapho.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import app.tapho.ui.login.model.LoginData
import com.google.gson.Gson

class AppSharedPreference {

    companion object {
        private lateinit var instance: AppSharedPreference
        private lateinit var preference: SharedPreferences
        fun getInstance(context: Context):AppSharedPreference {
            if (this::instance.isInitialized.not()) {
                instance = AppSharedPreference()
            }
            if (this::preference.isInitialized.not()) {
                preference = context.getSharedPreferences("TAPFO", MODE_PRIVATE)
            }
            return instance
        }
    }

    fun saveString(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return preference.getString(key, "")
    }

    fun saveBoolean(key: String,b:Boolean){
        preference.edit().putBoolean(key,b).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preference.getBoolean(key, false)
    }

    fun getLoginData(): LoginData? {
        return Gson().fromJson(getString(LOGIN_DATA), LoginData::class.java)
    }

    fun getUserId():String{
        getLoginData()?.id?.let {
            return it
       }
        return ""
    }

    fun clear(){
        preference.edit().clear().apply()
    }

}