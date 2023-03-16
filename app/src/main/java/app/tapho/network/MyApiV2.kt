package app.tapho.network


import androidx.databinding.library.BuildConfig
import app.tapho.TapfoApplication.Companion.applicationContext
import app.tapho.ui.Faqs.Model.Faqsmodel
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.RechargeService.ModelData.OperatorFatchModel.OpreatorfetchRes
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.RechargeService.ModelData.RechargeStatus.checkRechargeStatusRes
import app.tapho.ui.TapfoFi.Model.TapfoFiCategories.TapfoFiCategories_Res
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FiCategoriesMiniAppsRes
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GameAddToRecent.addGameToRecentList
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GameFavandUnFav.getFavUnFav
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.GetGamezopTag.GamezopTag
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.login.referral_Model.referral_code_res
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MinisRecentsRes
import app.tapho.ui.model.UserDetails.getUserDetailRes
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.scanner.model.TapfoMartProductRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface MyApiV2 {



    //Version 2
    @FormUrlEncoded
    @POST("loginOtp")
    suspend fun loginOtpV2(
        @Field("token") token: String,
    ): Response<LoginRes>


    @FormUrlEncoded
    @POST("verifyOtp")
    suspend fun verifyOtpV2(
        @Field("token") token: String,
    ): Response<LoginRes>


    @FormUrlEncoded
    @POST("loginCheckPasscode")
    suspend fun loginCheckPasscodeV2(
        @Field("token") token: String?,
    ): Response<BaseRes>

//Version 2

    @FormUrlEncoded
    @POST("verifyReferralCode")
    suspend fun verifyReferralCode(
        @Field("token") mobile: String,
    ): Response<referral_code_res>

    @FormUrlEncoded
    @POST("get_user_detail")
    suspend fun get_user_detail(
        @Field("token") token: String,
    ): Response<getUserDetailRes>

    @FormUrlEncoded
    @POST("setPasscode")
    suspend fun setPasscode(
        @Field("token") token: String,
    ): Response<LoginRes>



    @FormUrlEncoded
    @POST("update_profile")
    suspend fun updateProfile(
        @Field("token") token: String,
    ): Response<LoginRes>


    @FormUrlEncoded
    @POST("update_profile")
    suspend fun updateProfile2(
        @Field("token") token: String,
    ): Response<LoginRes>

    //    loginPasscode
    @FormUrlEncoded
    @POST("loginPasscode")
    suspend fun loginWithPasscode(
        @Field("token") token: String?,
    ): Response<LoginRes>


    @FormUrlEncoded
    @POST("get_home_screen")
    suspend fun getHomeData(
        @Field("token") token: String?
    ): Response<HomeRes>

    /**
     * cashback_merchant_category_id for all category send 0
     */
    @FormUrlEncoded
    @POST("get_cashback_merchant")
    suspend fun getCashbackMerchant(
        @Field("token") token: String?,
    ): Response<CashbackMerchantsAllRes>

    @FormUrlEncoded
    @POST("get_appCatgeory_miniApp")
    suspend fun getAppCategoryMiniApp(
        @Field("token") token: String?,
    ): Response<MiniAppRes>


    @FormUrlEncoded
    @POST("get_miniapp_tcash")
    suspend fun getMiniAppTCash(
        @Field("token") token: String?,
    ): Response<WebTCashRes>


    @FormUrlEncoded
    @POST("miniapp_fav")
    suspend fun miniAppFev(
        @Field("token") token: String?,
    ): Response<BaseRes>

    @FormUrlEncoded
    @POST("miniapp_unfav")
    suspend fun miniAppUnFev(
        @Field("token") token: String?,
        ): Response<BaseRes>

    @FormUrlEncoded
    @POST("miniapp_favlist")
    suspend fun getFavMiniApp(
        @Field("token") token: String?,
    ): Response<WebTCashRes>

    @FormUrlEncoded
    @POST("addMiniappRecently")
    suspend fun addMiniappRecently(
        @Field("token") token: String?,
    ): Response<BaseRes>

   @FormUrlEncoded
    @POST("getMiniappRecentlyList")
    suspend fun getMiniappRecentlyList(
        @Field("token") token: String?,
    ): Response<MinisRecentsRes>


    /**
     * @param type 0 pending, 1 approve, 2 all
     */
    @FormUrlEncoded
    @POST("tcash_dashboard")
    suspend fun getTCashDashboard(
        @Field("token") token: String?
    ): Response<TCashDasboardRes>

    /**
     * @param offer_type 1 category, 2 merchant deals
     */
    @FormUrlEncoded
    @POST("get_offers")
    suspend fun getOffers(
        @Field("token") token: String?
    ): Response<MerchantOfferRes>

    /**
     * @param type 2 offer, 3 coupons
     */
    @FormUrlEncoded
    @POST("get_offers_list")
    suspend fun getOffersDetail(
        @Field("token") token: String?
    ): Response<OfferDetailRes>

    /**
     * @param device_type 1
     */

    @FormUrlEncoded
    @POST("update_device_token")
    suspend fun updateNotification(
        @Field("token") token: String?,
    ): Response<BaseRes>

    /**
     * @ search all for all miniapps
     * @ search by id
     * @ Search by name
     */
    @FormUrlEncoded
    @POST("search_miniApp")
    suspend fun searchMiniApp(
        @Field("token") token: String?,
    ): Response<WebTCashRes>


    @FormUrlEncoded
    @POST("getRechargeService")
    suspend fun getRechargeService(@Field("token") token: String?): Response<RechargeServiceRes>

    @FormUrlEncoded
    @POST("getRechargeCircle")
    suspend fun getRechargeCircle(@Field("token") token: String?): Response<RechargeCircle>

    @FormUrlEncoded
    @POST("getRechargeServiceOperator")
    suspend fun getRechargeServiceOperator(
        @Field("token") token: String?,
    ): Response<ServiceOperatorRes>

    @FormUrlEncoded
    @POST("getOperatorFatch")
    suspend fun getRechargeOperatorFeatch(
        @Field("token") token: String?,
    ): Response<OpreatorfetchRes>

    @FormUrlEncoded
    @POST("getFatchPlan")
    suspend fun getRechargePlans(
        @Field("token") token: String?,
    ): Response<getRechargePlans>

    @FormUrlEncoded
    @POST("recharge")
    suspend fun rechargeprocess(
        @Field("token") token: String?,
    ): Response<RechargeDoneOrNotRes>


    /**
     * /1 popular, 2 trending Games
     */

    @FormUrlEncoded
    @POST("getGame")
    suspend fun getAllGames(
        @Field("token") token: String?,
    ): Response<Games>

    @FormUrlEncoded
    @POST("getGamezopCategory")
    suspend fun getGamezopCategoryData(@Field("token") token: String?): Response<GamezopCategoryData>

    @FormUrlEncoded
    @POST("getGamezopTag")
    suspend fun getGamezopTag(@Field("token") token: String?): Response<GamezopTag>

    @FormUrlEncoded
    @POST("getGameFavList")
    suspend fun getGameFavList(@Field("token") token: String?): Response<getGameFavList>

    @FormUrlEncoded
    @POST("get_faq")
    suspend fun getFAQs(@Field("token") token: String?): Response<Faqsmodel>

    @FormUrlEncoded
    @POST("addGameFavUnfav")
    suspend fun getGameFavandUnFav(
        @Field("token") token: String?,
    ): Response<getFavUnFav>

    @FormUrlEncoded
    @POST("addGameRecently")
    suspend fun getaddGametoRecentList(
        @Field("token") token: String?,
    ): Response<addGameToRecentList>

    @FormUrlEncoded
    @POST("getGameRecentlyList")
    suspend fun getGameRecentList(
        @Field("token") token: String?,
    ): Response<GameRecentPlayList>


    @FormUrlEncoded
    @POST("paytm_initiate_transaction")
    suspend fun get_initiate_transaction(
        @Field("token") token: String?,
    ): Response<InitiateTransactionRes2>

    @FormUrlEncoded
    @POST("paytm_process_transaction")
    suspend fun getTransactionProsses(
        @Field("token") token: String?,
    ): Response<TransactionProcessRes>

    @FormUrlEncoded
    @POST("paytm_transaction_status")
    suspend fun getTransactionStatus(
        @Field("token") token: String?,
    ): Response<TransactionStatusRes>

    /**

        * amount use for wallet Amount
        * payment_amount use for PSP Amount
        * type 1 Top-Up
        * type 2 Sign up Bonus
        * type 3 Mobile Recharge
        * type 4 Referral Bonus
        * type 5 Gift Voucher
        * type 6 Mobile Recharge Refund
        * type 7 Mobile Recharge Cashback

    **/

    @FormUrlEncoded
    @POST("add_user_txn")
    suspend fun addUserTransaction(
        @Field("token") token: String?,
    ): Response<addUserTransactionRes>

    @FormUrlEncoded
    @POST("checkRechargeStatus")
    suspend fun checkRechargeStatus(
        @Field("token") token: String?,
    ): Response<checkRechargeStatusRes>

//    @FormUrlEncoded
//    @POST("add_user_txn")
//    suspend fun addUserTransaction(
//        @Field("token") token: String?,
//    ): Response<addUserTransactionRes>

    @FormUrlEncoded
    @POST("get_notification")
    suspend fun getAllNotification(
        @Field("token") token: String?,
    ): Response<AllNotificationRes>

    @FormUrlEncoded
    @POST("get_wallet_slab")
    suspend fun getWalletSlab(
        @Field("token") token: String?
    ): Response<AddWalletVoucherRes>

    @FormUrlEncoded
    @POST("add_money_wallet")
    suspend fun addMoneyToWallet(
        @Field("token") token: String?,
    ): Response<AddMoneyRes>


//    TapfoFi

    @FormUrlEncoded
    @POST("get_finCatgeory")
    suspend fun TapfoFicategories(
        @Field("token") token:String?,
    ):Response<TapfoFiCategories_Res>

    @FormUrlEncoded
    @POST("get_finCatgeory_miniApp")
    suspend fun TapfoFiCategoriesMiniapps(
        @Field("token") token:String?,
    ):Response<FiCategoriesMiniAppsRes>

  @FormUrlEncoded
    @POST("searchBarcodeproduct")
    suspend fun TapfoMartsearchBarcodeproduct(
        @Field("token") token:String?,
    ):Response<TapfoMartProductRes>


    companion object {

        external fun BaseURL(): String?

        operator fun invoke(): MyApiV2 {
            System.loadLibrary("keys")
            val cacheDir = applicationContext().cacheDir
            //   val httpCacheDirectory = File(cacheDir, "offlineCache")
            val cache = Cache(cacheDir, 10 * 1024 * 1024)
            val logging = HttpLoggingInterceptor()
            val DecryptionInterceptor = DecryptionInterceptor()
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
                addInterceptor(DecryptionInterceptor)

                if (BuildConfig.DEBUG)
                    addInterceptor(logging)
            }.build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseURL()!!)
                .client(client)
                .build()
                .create(MyApiV2::class.java)
        }


    }


}