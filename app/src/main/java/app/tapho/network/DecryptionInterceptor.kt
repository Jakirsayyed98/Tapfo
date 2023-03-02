package app.tapho.network

import android.text.TextUtils
import android.util.Pair.create
import app.tapho.utils.decrypt
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class DecryptionInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            val newResponse: Response.Builder = response.newBuilder()
            var contentType: String? = response.header("Content-Type")
            if (TextUtils.isEmpty(contentType)) contentType = "application/json"
            val responseString: String? = response.body?.string()
            var decryptedString: String? = null

            try {
                decryptedString = decrypt(responseString)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            newResponse.body(decryptedString!!.toResponseBody())
            return newResponse.build()
        }
        return response
    }
}