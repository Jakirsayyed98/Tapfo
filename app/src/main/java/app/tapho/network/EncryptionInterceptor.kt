package app.tapho.network

import android.util.Log
import app.tapho.utils.bodyToString
import app.tapho.utils.encrypt
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class EncryptionInterceptor :Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val rawBody = request.body
        var encryptedBody = ""
        val mediaType = "text/plain; charset=utf-8".toMediaTypeOrNull()

        try {
            val rawBodyString: String = bodyToString(rawBody!!)
            val data = JsonObject().apply {
                addProperty(rawBodyString.split("=")[0],rawBodyString.split("=")[1].split("&")[0])
            }

            encryptedBody = encrypt(data.toString())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val body: RequestBody = RequestBody.create(mediaType, encryptedBody)
        //build new request
        request = request.newBuilder()
        .header("Content-Type", body.contentType().toString())
        .header("Content-Length", body.contentLength().toString())
        .method(request.method, body).build()
        return chain.proceed(request)
    }
}