package app.tapho.network


import androidx.databinding.library.BuildConfig
import app.tapho.TapfoApplication.Companion.applicationContext
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.ui.help.model.SupportServiceListRes
import app.tapho.ui.localbizzUI.Model.AddRating.AddBusinessRatingRes
import app.tapho.ui.localbizzUI.Model.BusinessCategories.BusinessCategory
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.BusinessSubCategory
import app.tapho.ui.localbizzUI.Model.BusinessTags.businesstagRes
import app.tapho.ui.localbizzUI.Model.BusinessType.business_types
import app.tapho.ui.localbizzUI.Model.SaveBusiness.SaveBusinessRes
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.SearchAllBusinessList
import app.tapho.ui.localbizzUI.Model.getBusinessDetails.getBusinessDetailRes
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.getBusinessListResForBusinessPerson
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.model.mini_app_data.MiniAppsDataRes
import app.tapho.utils.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface  MyApi {


    @FormUrlEncoded
    @POST("resendOtp")
    suspend fun resendOtp(
        @Field("token") token: String,
    ): Response<LoginRes>

/**


//    @Multipart
//    @POST("update_profile")
//    suspend fun updateProfile2(
//        @Part("token") token: RequestBody
////        @Part("user_id") id: RequestBody,
////        @Part("email") email: RequestBody,
////        @Part("name") name: RequestBody,
////        @Part("gender") gender: RequestBody,
////        @Part("dob") dob: RequestBody,
////        @Part image: MultipartBody.Part?,
//    ): Response<LoginRes>

**/

    @FormUrlEncoded
    @POST("supportServiceList")
    suspend fun getSupportServiceList(
        @Field("token") token: String?
    ): Response<SupportServiceListRes>

    /**
     * @param type 1 message ,2 feedback,3 request call,4 raised ticket
     */
    @FormUrlEncoded
    @POST("support")
    suspend fun support(
        @Field("token") token: String?
    ): Response<BaseRes>


//    @FormUrlEncoded
//    @POST("mini_app_data")
//    suspend fun searchMiniAppbyId(
//        @Field("token") token: String?,
//    ): Response<MiniAppsDataRes>




//    @FormUrlEncoded
//    @POST("getVoucherCategory")
//    suspend fun getVoucherCategory(
//        @Field("token") token: String?
//    ): Response<VoucherCategoriesViewmodelRes>
//
//    @FormUrlEncoded
//    @POST("getVoucherList")
//    suspend fun getVoucherList(
//        @Field("token") token: String?,
//    ): Response<VoucherListRes>
//
//    @FormUrlEncoded
//    @POST("getVoucherDetail")
//    suspend fun getVoucherDetails(
//        @Field("token") token: String?,
//    ): Response<voucherdatailRes>
//
//
//    @FormUrlEncoded
//    @POST("voucherPurchase")
//    suspend fun BuyVouchersApi(
//        @Field("token") token: String?,
//    ): Response<VoucherBuyingApiRes>


    @FormUrlEncoded
    @POST("getNews")
    suspend fun getNews(
        @Field("token") token: String?,
    ): Response<getAllNewsdata>

    @FormUrlEncoded
    @POST("getNewsCategory")
    suspend fun getNewsCategory(
        @Field("token") token: String?,
    ): Response<getCategories>



    @FormUrlEncoded
    @POST("story")
    suspend fun getStoriesData(
        @Field("token") token: String?,
    ): Response<StoriesResFile>



    //start Bussiness Api

    @FormUrlEncoded
    @POST("getBusinessType")
    suspend fun getBusinessType(
        @Field("token") token: String?,
    ): Response<business_types>

    @FormUrlEncoded
    @POST("getBusinessCategory")
    suspend fun getBusinessCategory(
        @Field("token") token: String?,
    ): Response<BusinessCategory>

    @FormUrlEncoded
    @POST("getBusinessSubCategory")
    suspend fun getBusinessSubCategory(
        @Field("token") token: String?,
    ): Response<BusinessSubCategory>

    @Multipart
    @POST("saveBusiness")
    suspend fun saveBusiness(
        @Part("user_id") user_id: RequestBody?,
        @Part("business_type_id") business_type_id: RequestBody?,
        @Part("business_category_id") business_category_id: RequestBody?,
        @Part("business_subcategory_id") business_subcategory_id: RequestBody?,
        @Part("business_name") business_name: RequestBody?,
        @Part("pancard") pancard: RequestBody?,
        @Part("gst") gst: RequestBody?,
//        @Field("image") image: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @Part logo: MultipartBody.Part?,
        @Part("description") description: RequestBody?,
        @Part("contacts") contacts: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("whatsapp_business") whatsapp_business: RequestBody?,
        @Part("established_year") established_year: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("floor") floor: RequestBody?,
        @Part("area") area: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("landmark") landmark: RequestBody?,
        @Part("temparary_close") temparary_close: RequestBody?,
        @Part("always_open") always_open: RequestBody?,
        @Part("sun_opening") sun_opening: RequestBody?,
        @Part("sun_closing") sun_closing: RequestBody?,
        @Part("mon_opening") mon_opening: RequestBody?,
        @Part("mon_closing") mon_closing: RequestBody?,
        @Part("tue_opening") tue_opening: RequestBody?,
        @Part("tue_closing") tue_closing: RequestBody?,
        @Part("wed_opening") wed_opening: RequestBody?,
        @Part("wed_closing") wed_closing: RequestBody?,
        @Part("thu_opening") thu_opening: RequestBody?,
        @Part("thu_closing") thu_closing: RequestBody?,
        @Part("fri_opening") fri_opening: RequestBody?,
        @Part("fri_closing") fri_closing: RequestBody?,
        @Part("sat_opening") sat_opening: RequestBody?,
        @Part("sat_closing") sat_closing: RequestBody?,
        @Part("editid") editid: RequestBody?,
        @Part("business_tag_id") business_tag_id: RequestBody?,
    ): Response<SaveBusinessRes>


    @FormUrlEncoded
    @POST("getBusinessTag")
    suspend fun getBusinessTag(
        @Field("token") token: String?,
    ): Response<businesstagRes>

    @FormUrlEncoded
    @POST("getBusinessDetail")
    suspend fun getBusinessDetails(
        @Field("token") token: String?,
    ): Response<getBusinessDetailRes>

    @FormUrlEncoded
    @POST("getBusinessList")
    suspend fun getbusinessListForBusinessPerson(
        @Field("token") token: String?,
    ): Response<getBusinessListResForBusinessPerson>

    @FormUrlEncoded
    @POST("businessRatingAdd")
    suspend fun AddBusinessRating( @Field("token") token: String?, ): Response<AddBusinessRatingRes>

    @FormUrlEncoded
    @POST("searchBusinessList")
    suspend fun getBusinessListForUser( @Field("token") token: String?, ): Response<SearchAllBusinessList>



    companion object {

        external fun BaseURL(): String?

        operator fun invoke(): MyApi {

            System.loadLibrary("keys")


            val cacheDir = applicationContext().cacheDir
            //   val httpCacheDirectory = File(cacheDir, "offlineCache")
            val cache = Cache(cacheDir, 10 * 1024 * 1024)


            val logging = HttpLoggingInterceptor()
            val DecryptionInterceptor = DecryptionInterceptor()

            val cacheOnline = CacheInterceptor()
            val cacheOffline = OfflineCacheInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().apply {
                callTimeout(30, TimeUnit.SECONDS)
                cache(cache)

                addInterceptor { chain ->
                    var request = chain.request()
                    request = if (hasNetwork(applicationContext())!!) {
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                    } else{
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                }

                    chain.proceed(request)
                }

//                addNetworkInterceptor(cacheOnline)
//                addInterceptor(cacheOffline)
//                addInterceptor(DecryptionInterceptor)

                if (BuildConfig.DEBUG)
                    addInterceptor(logging)
            }.build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseURL()!!)
                .client(client)
                .build()
                .create(MyApi::class.java)
        }


    }


}