package app.tapho.RechargeApiNetwork

import app.tapho.BuildConfig
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface RechargeApis {
    @FormUrlEncoded
    @POST("CyrusPlanFatchAPI.aspx?APIID=AP109198&PASSWORD=Jf67c7b8Hf6g")
    suspend fun AllRechargePlans(
        @Field("Operator_Code") opraterCode: String,
        @Field("Circle_Code") Circle_Code: String,
        @Field("MobileNumber") MobileNumber: String,
        @Field("PageID") PageID: String,
        @Field("FORMAT") FORMAT: String,

    ): Response<getRechargePlans>

    companion object {

        operator fun invoke(): RechargeApis {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().apply {
                callTimeout(30, TimeUnit.SECONDS)
                if (BuildConfig.DEBUG)
                    addInterceptor(logging)
            }.build()
            return Retrofit.Builder()
                .baseUrl("https://cyrusrecharge.in/API/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RechargeApis::class.java)

        }
    }
}