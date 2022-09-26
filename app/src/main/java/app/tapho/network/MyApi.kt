package app.tapho.network

//import app.tapho.BuildConfig
import android.util.Log
import androidx.databinding.library.BuildConfig
import app.tapho.TapfoApplication
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.BuyVoucher.VoucherDetails.voucherdatailRes
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.Faqs.Model.Faqsmodel
import app.tapho.ui.LeaderShip.Model.leaderboardRes
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory.getNotificationCategory
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.NotificationListRes
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.RechargeService.ModelData.OperatorFatchModel.getRechargeoperatorFeatch
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GameAddToRecent.addGameToRecentList
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GameFavandUnFav.getFavUnFav
import app.tapho.ui.games.models.GamesCategoryRes
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.GetGamezopTag.GamezopTag
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.help.model.SupportServiceListRes
import app.tapho.ui.home.ShopProduct.Model.ShopCategory.ShopCategory
import app.tapho.ui.home.ShopProduct.Model.ShopChildCategory.ShopChildCategory
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.Model.ShopSubCategory.ShopSubCategory
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
import app.tapho.ui.login.referral_Model.referral_code_res
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.model.NotificationRes
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.payment.model.TransactionTokenRes
import app.tapho.ui.recharge.model.ServiceOperatorRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface MyApi {
    @FormUrlEncoded
    @POST("loginOtp")
    suspend fun loginOtp(
        @Field("mobile_no") mobile: String,
        @Field("device_id") appKey: String,
        @Field("country_code") country_code: String,
    ): Response<LoginRes>

  @FormUrlEncoded
    @POST("verifyReferralCode")
    suspend fun verifyReferralCode(
        @Field("user_id") mobile: String,
        @Field("referral_code") appKey: String,
    ): Response<referral_code_res>

    @FormUrlEncoded
    @POST("resendOtp")
    suspend fun resendOtp(
        @Field("mobile_no") mobile: String,
        @Field("country_code") country_code: String,
    ): Response<LoginRes>


    @FormUrlEncoded
    @POST("verifyOtp")
    suspend fun verifyOtp(
        @Field("user_id") user_id: String,
        @Field("otp") otp: String,
    ): Response<LoginRes>

    @FormUrlEncoded
    @POST("setPasscode")
    suspend fun setPasscode(
        @Field("user_id") user_id: String,
        @Field("passcode") passcode: String,
    ): Response<LoginRes>



    @FormUrlEncoded
    @POST("update_profile")
    suspend fun updateProfile(
        @Field("user_id") user_id: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("image") image: String,
        @Field("referral_code") referral_code: String,
    ): Response<LoginRes>

    //    loginPasscode
    @FormUrlEncoded
    @POST("loginPasscode")
    suspend fun loginWithPasscode(
        @Field("passcode") passcode: String?,
        @Field("country_code") country_code: String?,
        @Field("mobile_no") mobile_no: String?,
    ): Response<LoginRes>

    @FormUrlEncoded
    @POST("loginCheckPasscode")
    suspend fun loginCheckPasscode(
        @Field("country_code") country_code: String?,
        @Field("mobile_no") mobile_no: String?,
    ): Response<BaseRes>

    @FormUrlEncoded
    @POST("get_home_screen")
    suspend fun getHomeData(
        @Field("user_id") user_id: String?,
    ): Response<HomeRes>

    @FormUrlEncoded
    @POST("get_home_screen")
    suspend fun getHomeDataNew(
        @Field("user_id") user_id: String?,
    ): Response<HomeResData>

    /**
     * cashback_merchant_category_id for all category send 0
     */
    @FormUrlEncoded
    @POST("get_cashback_merchant")
    suspend fun getCashbackMerchant(
        @Field("user_id") user_id: String?,
        @Field("cashback_merchant_category_id") cashback_merchant_category_id: String?,
    ): Response<CashbackMerchantsAllRes>

    @FormUrlEncoded
    @POST("get_appCatgeory_miniApp")
    suspend fun getAppCategoryMiniApp(
        @Field("user_id") user_id: String?,
        @Field("app_category_id") app_category_id: String?,
    ): Response<MiniAppRes>

    @FormUrlEncoded
    @POST("get_appCatgeory_miniApp")
    suspend fun getAppCategoryMiniAppwithBanner(
        @Field("user_id") user_id: String?,
        @Field("app_category_id") app_category_id: String?,
    ): Response<MiniAppRes1>

    @FormUrlEncoded
    @POST("get_miniapp_tcash")
    suspend fun getMiniAppTCash(
        @Field("user_id") user_id: String?,
        @Field("mini_app_id") mini_app_id: String?,
    ): Response<WebTCashRes>

    @FormUrlEncoded
    @POST("get_miniapp_tcash")
    suspend fun getMiniAppTCash1(
        @Field("user_id") user_id: String?,
        @Field("mini_app_id") mini_app_id: String?,
    ): Response<app.tapho.ui.newAPI.WebTCashRes>

    @FormUrlEncoded
    @POST("miniapp_fav")
    suspend fun miniAppFev(
        @Field("user_id") user_id: String?,
        @Field("mini_app_id") mini_app_id: String?,
    ): Response<BaseRes>

    @FormUrlEncoded
    @POST("miniapp_unfav")
    suspend fun miniAppUnFev(
        @Field("user_id") user_id: String?,
        @Field("mini_app_id") mini_app_id: String?,
    ): Response<BaseRes>

    @FormUrlEncoded
    @POST("miniapp_favlist")
    suspend fun getFavMiniApp(
        @Field("user_id") user_id: String?,
    ): Response<WebTCashRes>

    @FormUrlEncoded
    @POST("miniapp_favlist")
    suspend fun getFavMiniApp2(
        @Field("user_id") user_id: String?,
    ): Response<WebTCashRes>

    @FormUrlEncoded
    @POST("tcash_dashboard")
    suspend fun getTCashDashboard(
        @Field("user_id") user_id: String?,
        @Field("start_date") start_date: String?,
        @Field("end_date") end_date: String?,
    ): Response<TCashDasboardRes>

    /**
     * @param offer_type 1 category, 2 merchant deals
     */
    @FormUrlEncoded
    @POST("get_offers")
    suspend fun getOffers(
        @Field("user_id") user_id: String?,
        @Field("offer_type") offer_type: String?,
    ): Response<MerchantOfferRes>

    /**
     * @param type 2 offer, 3 coupons
     */
    @FormUrlEncoded
    @POST("get_offers_list")
    suspend fun getOffersDetail(
        @Field("user_id") user_id: String?,
        @Field("offer_type") offer_type: String?,
        @Field("app_category_id") app_category_id: String?,
        @Field("category_id") category_id: String?,
        @Field("type") type: String?,
    ): Response<OfferDetailRes>

    @FormUrlEncoded
    @POST("supportServiceList")
    suspend fun getSupportServiceList(@Field("user_id") user_id: String?): Response<SupportServiceListRes>

    /**
     * @param type 1 message ,2 feedback,3 request call,4 raised ticket
     */
    @FormUrlEncoded
    @POST("support")
    suspend fun support(
        @Field("user_id") user_id: String?,
        @Field("subject") subject: String?,
        @Field("message") message: String?,
        @Field("service") service: String?,
        @Field("type") type: String?,
    ): Response<BaseRes>

    @Multipart
    @POST("update_profile")
    suspend fun updateProfile2(
        @Part("user_id") id: RequestBody?,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Response<LoginRes>

    @FormUrlEncoded
    @POST("get_notification_list")
    suspend fun getNotificationList(@Field("user_id") user_id: String?): Response<NotificationRes>

    /**
     * @param device_type 1
     */
    @FormUrlEncoded
    @POST("update_device_token")
    suspend fun updateNotification(
        @Field("user_id") user_id: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?,
    ): Response<BaseRes>

    @FormUrlEncoded
    @POST("search_miniApp")
    suspend fun searchMiniApp(
        @Field("user_id") user_id: String?,
        @Field("search") search: String?,
    ): Response<WebTCashRes>

    @FormUrlEncoded
    @POST("get_wallet_slab")
    suspend fun getOfferVouchers(@Field("user_id") user_id: String?): Response<AddWalletVoucherRes>

    @FormUrlEncoded
    @POST("add_money_wallet")
    suspend fun addMoneyToWallet(
        @Field("user_id") user_id: String?,
        @Field("amount") amount: String?,
        @Field("cashback") cashback: String?,
        @Field("txn_id") txn_id: String?,
        @Field("pay_option") pay_option: String?,
    ): Response<AddMoneyRes>

    @FormUrlEncoded
    @POST("getVoucherCategory")
    suspend fun getVoucherCategory(@Field("user_id") user_id: String?): Response<VoucherCategoriesViewmodelRes>

    @FormUrlEncoded
    @POST("getVoucherList")
    suspend fun getVoucherList(
        @Field("user_id") user_id: String?,
        @Field("voucher_category_id") voucher_category_id: String?,
        @Field("voucher_sub_category_id") voucher_sub_category_id: String?,
    ): Response<VoucherListRes>

    @FormUrlEncoded
    @POST("getVoucherDetail")
    suspend fun getVoucherDetails(@Field("user_id") user_id: String?,@Field("voucher_id") voucher_id: String?): Response<voucherdatailRes>


    @FormUrlEncoded
    @POST
    suspend fun getTransactionToken(
        @Url url: String,
        @Field("user_id") user_id: String?,
        @Field("amount") amount: Double,
        @Field("order_id") order_id: String,
    ): Response<TransactionTokenRes>

    @FormUrlEncoded
    @POST("getRechargeService")
    suspend fun getRechargeService(@Field("user_id") user_id: String?): Response<RechargeServiceRes>

    @FormUrlEncoded
    @POST("getRechargeCircle")
    suspend fun getRechargeCircle(@Field("user_id") user_id: String?): Response<RechargeCircle>

    @FormUrlEncoded
    @POST("getRechargeServiceOperator")
    suspend fun getRechargeServiceOperator(
        @Field("user_id") user_id: String?,
        @Field("rechargeservicestype_id") rechargeservicestype_id: String?,
    ): Response<ServiceOperatorRes>

    @FormUrlEncoded
    @POST("getOperatorFatch")
    suspend fun getRechargeOperatorFeatch(
        @Field("user_id") user_id: String?,
        @Field("number") number: String?,
    ): Response<getRechargeoperatorFeatch>

    @FormUrlEncoded
    @POST("getFatchPlan")
    suspend fun getRechargePlans(
        @Field("user_id") user_id: String?,
        @Field("number") number: String?,
        @Field("operator_id") operator_id: String?,
        @Field("circle_id") circle_id: String?,
    ): Response<getRechargePlans>

    @FormUrlEncoded
    @POST("recharge")
    suspend fun rechargeprocess(
        @Field("user_id") user_id: String?,
        @Field("recharge_type") recharge_type: String?,
        @Field("number") number: String?,
        @Field("operator_id") operator_id: String?,
        @Field("circle_id") circle_id: String?,
        @Field("amount") amount: String?,
        @Field("user_txn_id") user_txn_id: String?,
    ): Response<RechargeDoneOrNotRes>

    @FormUrlEncoded
    @POST("getGamezopCategory")
    suspend fun getGamezopCategory(@Field("user_id") user_id: String?): Response<GamesCategoryRes>

    @FormUrlEncoded
    @POST("getGame")
    suspend fun getAllGames(@Field("user_id") user_id: String?): Response<Games>

    @FormUrlEncoded
    @POST("getGamezopCategory")
    suspend fun getGamezopCategoryData(@Field("user_id") user_id: String?): Response<GamezopCategoryData>

    @FormUrlEncoded
    @POST("getGamezopTag")
    suspend fun getGamezopTag(@Field("user_id") user_id: String?): Response<GamezopTag>

    @FormUrlEncoded
    @POST("getGameFavList")
    suspend fun getGameFavList(@Field("user_id") user_id: String?): Response<getGameFavList>

    @FormUrlEncoded
    @POST("get_faq")
    suspend fun getFAQs(@Field("user_id") user_id: String?): Response<Faqsmodel>

    @FormUrlEncoded
    @POST("addGameFavUnfav")
    suspend fun getGameFavandUnFav(
        @Field("user_id") user_id: String?,
        @Field("game_id") game_id: String?,
        @Field("favUnfav") favUnfav: String?
    ): Response<getFavUnFav>

    @FormUrlEncoded
    @POST("addGameRecently")
    suspend fun getaddGametoRecentList(
        @Field("user_id") user_id: String?,
        @Field("game_id") game_id: String?,
    ): Response<addGameToRecentList>

    @FormUrlEncoded
    @POST("getGameRecentlyList")
    suspend fun getGameRecentList(
        @Field("user_id") user_id: String?,
    ): Response<GameRecentPlayList>

    @FormUrlEncoded
    @POST("getShopCategory")
    suspend fun getShopCategory(
        @Field("user_id") user_id: String?,
    ): Response<ShopCategory>

    @FormUrlEncoded
    @POST("getShopSubCategory")
    suspend fun getShopSubCategory(
        @Field("user_id") user_id: String?,
        @Field("category_id") category_id: String?,
    ): Response<ShopSubCategory>

    @FormUrlEncoded
    @POST("getShopChildCategory")
    suspend fun getShopChildCategory(
        @Field("user_id") user_id: String?,
        @Field("category_id") category_id: String?,
        @Field("sub_category_id") sub_category_id: String?,
    ): Response<ShopChildCategory>

    @FormUrlEncoded
    @POST("getShopProduct")
    suspend fun getShopProduct(
        @Field("user_id") user_id: String?,
        @Field("category_id") category_id: String?,
        @Field("sub_category_id") sub_category_id: String?,
        @Field("child_category_id") child_category_id: String?,
    ): //ShopProductData
            Response<ShopProductData>

    @FormUrlEncoded
    @POST("getNews")
    suspend fun getNews(
        @Field("user_id") user_id: String?,
        @Field("id") id: String?,
        @Field("page") page: String?,
    ): Response<getAllNewsdata>

    @FormUrlEncoded
    @POST("getNewsCategory")
    suspend fun getNewsCategory(
        @Field("user_id") user_id: String?,
    ): Response<getCategories>


    @FormUrlEncoded
    @POST("leaderBoard")
    suspend fun getLeaderBoardData(
        @Field("") user_id: String?,
        ): Response<leaderboardRes>

    @FormUrlEncoded
    @POST("get_notification_category")
    suspend fun getNewNotificationCategory(@Field("user_id") user_id: String?): Response<getNotificationCategory>

    @FormUrlEncoded
    @POST("get_notification_list")
    suspend fun getNewNotification(
        @Field("user_id") user_id: String?,
        @Field("id") id: String?,

        ): Response<NotificationListRes>

    @FormUrlEncoded
    @POST("story")
    suspend fun getStoriesData(
        @Field("user_id") user_id: String?,
    ): Response<StoriesResFile>


    @FormUrlEncoded
    @POST("paytm_initiate_transaction")
    suspend fun get_initiate_transaction(
       // @Url url: String,
        @Field("user_id") user_id: String?,
        @Field("amount") amount: Double,
        @Field("order_id") order_id: String,
    ): Response<InitiateTransactionRes>


    @FormUrlEncoded
    @POST("paytm_process_transaction")
    suspend fun getTransactionProsses(
       // @Url url: String,
        @Field("user_id") user_id: String?,
        @Field("order_id") order_id: String,
        @Field("payment_mode") payment_mode: String,
        @Field("card_info") card_info: String,
        @Field("txnToken") txnToken: String,
    ): Response<TransactionProcessRes>

    @FormUrlEncoded
    @POST("paytm_transaction_status")
    suspend fun getTransactionStatus(
     //   @Url url: String,
        @Field("user_id") user_id: String?,
        @Field("order_id") order_id: String,
    ): Response<TransactionStatusRes>

    @FormUrlEncoded
    @POST("add_user_txn")
    suspend fun addUserTransaction(
        @Field("user_id") user_id: String?,
        @Field("amount") amount: String,
        @Field("payment_amount") payment_amount: String,
        @Field("txn_id") txn_id: String,
        @Field("pay_option") pay_option: String,
        @Field("type") type: String,
    ): Response<addUserTransactionRes>


    @FormUrlEncoded
    @POST("get_notification")
    suspend fun getAllNotification(
        @Field("user_id") user_id: String?,
    ): Response<AllNotificationRes>

    //start Bussiness Api

    @FormUrlEncoded
    @POST("getBusinessType")
    suspend fun getBusinessType(
        @Field("user_id") user_id: String?,
    ): Response<business_types>

    @FormUrlEncoded
    @POST("getBusinessCategory")
    suspend fun getBusinessCategory(
        @Field("user_id") user_id: String?,
    ): Response<BusinessCategory>

    @FormUrlEncoded
    @POST("getBusinessSubCategory")
    suspend fun getBusinessSubCategory(
        @Field("user_id") user_id: String?,
        @Field("category_id") category_id: String?,
    ): Response<BusinessSubCategory>

    @Multipart
    @POST("saveBusiness")
    suspend fun saveBusiness(
        @Part ("user_id") user_id:  RequestBody?,
        @Part ("business_type_id") business_type_id:  RequestBody?,
        @Part ("business_category_id") business_category_id:  RequestBody?,
        @Part ("business_subcategory_id") business_subcategory_id:  RequestBody?,
        @Part ("business_name") business_name:  RequestBody?,
        @Part ("pancard") pancard:  RequestBody?,
        @Part ("gst") gst:  RequestBody?,
//        @Field("image") image: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @Part logo: MultipartBody.Part?,
        @Part ("description") description:  RequestBody?,
        @Part ("contacts") contacts:  RequestBody?,
        @Part ("email") email:  RequestBody?,
        @Part ("whatsapp_business") whatsapp_business:  RequestBody?,
        @Part ("established_year") established_year:  RequestBody?,
        @Part ("latitude") latitude:  RequestBody?,
        @Part ("longitude") longitude:  RequestBody?,
        @Part ("address") address:  RequestBody?,
        @Part ("floor") floor:  RequestBody?,
        @Part ("area") area:  RequestBody?,
        @Part ("city") city:  RequestBody?,
        @Part ("pincode") pincode:  RequestBody?,
        @Part ("landmark") landmark:  RequestBody?,
        @Part ("temparary_close") temparary_close:  RequestBody?,
        @Part ("always_open") always_open:  RequestBody?,
        @Part ("sun_opening") sun_opening:  RequestBody?,
        @Part ("sun_closing") sun_closing:  RequestBody?,
        @Part ("mon_opening") mon_opening:  RequestBody?,
        @Part ("mon_closing") mon_closing:  RequestBody?,
        @Part ("tue_opening") tue_opening:  RequestBody?,
        @Part ("tue_closing") tue_closing:  RequestBody?,
        @Part ("wed_opening") wed_opening:  RequestBody?,
        @Part ("wed_closing") wed_closing:  RequestBody?,
        @Part ("thu_opening") thu_opening:  RequestBody?,
        @Part ("thu_closing") thu_closing:  RequestBody?,
        @Part ("fri_opening") fri_opening:  RequestBody?,
        @Part ("fri_closing") fri_closing:  RequestBody?,
        @Part ("sat_opening") sat_opening:  RequestBody?,
        @Part ("sat_closing") sat_closing:  RequestBody?,
        @Part ("editid") editid:  RequestBody?,
        @Part ("business_tag_id") business_tag_id:  RequestBody?,
    ): Response<SaveBusinessRes>


    @FormUrlEncoded
    @POST("getBusinessTag")
    suspend fun getBusinessTag(
        @Field("user_id") user_id: String?,
        @Field("business_category_id") business_category_id: String?,
        @Field("business_subcategory_id") business_subcategory_id: String?,
    ): Response<businesstagRes>

    @FormUrlEncoded
    @POST("getBusinessDetail")
    suspend fun getBusinessDetails(
        @Field("user_id") user_id: String?,
        @Field("business_id") business_id: String?,
    ): Response<getBusinessDetailRes>

    @FormUrlEncoded
    @POST("getBusinessList")
    suspend fun getbusinessListForBusinessPerson(
        @Field("user_id") user_id: String?,
    ): Response<getBusinessListResForBusinessPerson>

    @FormUrlEncoded
    @POST("businessRatingAdd")
    suspend fun AddBusinessRating(
        @Field("user_id") user_id: String?,
        @Field("business_id") business_id: String?,
        @Field("rating") rating: String?,
        @Field("review") review: String?,
    ): Response<AddBusinessRatingRes>

    @FormUrlEncoded
    @POST("searchBusinessList")
    suspend fun getBusinessListForUser(
        @Field("user_id") user_id: String?,
        @Field("business_category_id") business_category_id: String?,
        @Field("business_subcategory_id") business_subcategory_id: String?,
        @Field("business_name") business_name: String?,
        @Field("latitude") latitude: String?,
        @Field("longitude") longitude: String?,
        @Field("distance") distance: String?,
        @Field("business_tag_id") business_tag_id: String?,
    ): Response<SearchAllBusinessList>





    companion object {


        operator fun invoke(): MyApi {
            val cacheSize = 100 * 1024 * 1024 // 10 MiB
            val cache = Cache(TapfoApplication.applicationContext().cacheDir, cacheSize.toLong())
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().apply {
                callTimeout(30, TimeUnit.SECONDS)
                addNetworkInterceptor(onlineInterceptor)
                addInterceptor(offlineInterceptor)
                cache(cache)
                if (BuildConfig.DEBUG)
                    addInterceptor(logging)
            }
                .build()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://tapfo.in/delta/api/")
                .client(client)
                .build()
                .create(MyApi::class.java)
        }


        var onlineInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }

        var offlineInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(TapfoApplication.applicationContext())) {
                val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(30, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .cacheControl(cacheControl)
                    .build()
                Log.d("CheckingOfflineCache",request.url.toString())
            }
            chain.proceed(request)
        }


    }
}