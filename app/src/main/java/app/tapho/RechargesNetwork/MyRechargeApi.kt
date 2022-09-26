package app.tapho.RechargesNetwork

import androidx.databinding.library.BuildConfig
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyRechargeApi {
//    @FormUrlEncoded
//    @POST("CyrusOperatorFatchAPI.aspx?APIID=AP109198&PASSWORD=Ftc7b8n&v7j")
//    suspend fun OperatorFatchAPI(
//        @Path("MOBILENUMBER") MOBILENUMBER: String?,
//        @Path("FORMAT") FORMAT: String?,
//    ): Response<OperatorFatchAPI>

    @FormUrlEncoded
    @POST("CyrusPlanFatchAPI.aspx?APIID=AP109198&PASSWORD=Jf67c7b8Hf6g")
    suspend fun AllRechargePlans(
        @Field("Operator_Code") opraterCode: String?,
        @Field("Circle_Code") Circle_Code: String?,
        @Field("MobileNumber") MobileNumber: String?,
        @Field("PageID") PageID: String?,
        @Field("FORMAT") FORMAT: String?,

        ): Response<getRechargePlans>

    companion object {
        operator fun invoke(): MyRechargeApi {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().apply {
                callTimeout(30, TimeUnit.SECONDS)
                if (BuildConfig.DEBUG)
                    addInterceptor(logging)
            }.build()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://cyrusrecharge.in/API/")
                .client(client)
                .build()
                .create(MyRechargeApi::class.java)
        }
    }
}