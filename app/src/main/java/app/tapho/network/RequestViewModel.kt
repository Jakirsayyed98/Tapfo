package app.tapho.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.BuyVoucher.VoucherDetails.voucherdatailRes
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.Faqs.Model.Faqsmodel
import app.tapho.ui.LeaderShip.Model.leaderboardRes
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.ui.RechargeService.ModelData.OperatorFatchModel.getRechargeoperatorFeatch
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GameAddToRecent.addGameToRecentList
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GameFavandUnFav.getFavUnFav
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.GetGamezopTag.GamezopTag
import app.tapho.ui.help.model.SupportServiceListRes
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.ShopProduct.Model.ShopCategory.ShopCategory
import app.tapho.ui.home.ShopProduct.Model.ShopChildCategory.ShopChildCategory
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.Model.ShopSubCategory.ShopSubCategory
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.NotificationRes
import app.tapho.ui.payment.model.TransactionTokenRes
import app.tapho.ui.recharge.model.ServiceOperatorRes
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory.getNotificationCategory
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.NotificationListRes
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.ui.localbizzUI.Model.AddRating.AddBusinessRatingRes
import app.tapho.ui.localbizzUI.Model.BusinessCategories.BusinessCategory
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.BusinessSubCategory
import app.tapho.ui.localbizzUI.Model.BusinessTags.businesstagRes
import app.tapho.ui.localbizzUI.Model.BusinessType.business_types
import app.tapho.ui.localbizzUI.Model.SaveBusiness.SaveBusinessRes
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.SearchAllBusinessList
import app.tapho.ui.localbizzUI.Model.getBusinessDetails.getBusinessDetailRes
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.getBusinessListResForBusinessPerson
import app.tapho.ui.login.referral_Model.referral_code_res
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.setErrorHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RequestViewModel : ViewModel() {
    private var loadLis: LoaderListener? = null

    fun loginWithOtp(
        mobile: String,
        device_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
//         loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().loginOtp(mobile, device_id, "91").body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }
fun verifyReferralCode(
        userid: String,
        referral_code: String,
        loadLis: LoaderListener,
        listener: ApiListener<referral_code_res, Any?>,
    ) {
        this.loadLis = loadLis
         loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().verifyReferralCode(userid, referral_code).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun resendOtp(
        mobile: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
            loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().resendOtp(mobile, "91").body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun verifyOtp(
        userId: String,
        otp: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
           loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().verifyOtp(userId, otp).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun setPasscode(
        userId: String,
        passcode: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
             loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().setPasscode(userId, passcode).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun updateProfile(
        userId: String,
        email: String,
        name: String,
        gender: String,
        dob: String,
        referral_code: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
            loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().updateProfile(userId, email, name, gender, dob, "", referral_code).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun loginWithPasscode(
        passCode: String?,
        countryCode: String?,
        mobile: String?,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
           loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().loginWithPasscode(passCode, countryCode, mobile).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun loginCheckPasscode(
        mobile: String?,
        type: String,
        loadLis: LoaderListener,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
       //    loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().loginCheckPasscode("91", mobile).body()
                    ?.let {
                        it.type = type
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getHomeData(
        type: String,
        userid: String?,
        loadLis: LoaderListener,
        listener: ApiListener<HomeRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {

                MyApi().getHomeData(userid).body()
                    ?.let {
                        it.type = type
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getHomeDataNew(
        type: String,
        userid: String?,
        loadLis: LoaderListener,
        listener: ApiListener<HomeResData, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getHomeDataNew(userid).body()
                    ?.let {
                        //   it.type = type
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getCashbackMerchantAll(
        userid: String?,
        cashbackMerchantCatId: String?,
        loadLis: LoaderListener,
        listener: ApiListener<CashbackMerchantsAllRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getCashbackMerchant(userid, cashbackMerchantCatId).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getAppCategoryMiniApp(
        userid: String?,
        app_category_id: String?,
        loadLis: LoaderListener,
        listener: ApiListener<MiniAppRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getAppCategoryMiniApp(userid, app_category_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getAppCategoryMiniAppwithBanner(
        userid: String?,
        app_category_id: String?,
        loadLis: LoaderListener,
        listener: ApiListener<MiniAppRes1, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getAppCategoryMiniAppwithBanner(userid, app_category_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getMiniAppTCash(
        userid: String?,
        mini_app_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<WebTCashRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getMiniAppTCash(userid, mini_app_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getMiniAppTCash1(
        userid: String?,
        mini_app_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<app.tapho.ui.newAPI.WebTCashRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getMiniAppTCash1(userid, mini_app_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun AddMoneyToWallet(
        userid: String?,
        amount: String?,
        cashback: String?,
        txn_id: String?,
        pay_option: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<AddMoneyRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().addMoneyToWallet(userid,amount,cashback,txn_id,pay_option).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun miniAppFev(
        userid: String?,
        mini_app_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().miniAppFev(userid, mini_app_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun miniAppUnFev(
        userid: String?,
        mini_app_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().miniAppUnFev(userid, mini_app_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getFavMiniApp(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<WebTCashRes, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getFavMiniApp(userid).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getTCashDashboard(
        userid: String?,
        startDate: String?,
        endDate: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<TCashDasboardRes, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getTCashDashboard(userid, startDate, endDate).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getOffers(
        userid: String?,
        offer_type: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<MerchantOfferRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getOffers(userid, offer_type).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getOfferDetail(
        userid: String?,
        offer_type: String?,
        app_category_id: String?,
        category_id: String?,
        type: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<OfferDetailRes, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getOffersDetail(userid, offer_type, app_category_id, category_id, type)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getSupportServiceList(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<SupportServiceListRes, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getSupportServiceList(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun support(
        userid: String?,
        subject: String?,
        message: String?,
        service: String?,
        type: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().support(userid, subject, message, service, type)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun updateProfile2(
        id: String?,
        email: String,
        name: String,
        gender: String,
        dob: String,
        image: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        var imagePart: MultipartBody.Part? = null

        if (image.isNullOrEmpty().not()) {
            val imageFile = File(image)
            val requestFile: RequestBody =
                imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        }

        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().updateProfile2(
                    id?.toRequestBody(),
                    email.toRequestBody(),
                    name.toRequestBody(),
                    gender.toRequestBody(),
                    dob.toRequestBody(),
                    imagePart
                ).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }


    fun getNotificationList(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<NotificationRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNotificationList(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getNewNotificationList(
        userid: String?,
        id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<NotificationListRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNewNotification(userid, id)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getNotificationCategory(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<getNotificationCategory, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNewNotificationCategory(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun updateNotification(
        userid: String?,
        deviceToken: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().updateNotification(userid, "2", deviceToken)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun searchMiniApp(
        userid: String?,
        search: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<WebTCashRes, Any?>,
    ): Job {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        return viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().searchMiniApp(userid, search)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getWalletOfferVouchers(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<AddWalletVoucherRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getOfferVouchers(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getVoucherCategory(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<VoucherCategoriesViewmodelRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getVoucherCategory(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getVoucherList(
        userid: String?,
        voucherCategoryId: String?,
        voucherSubcategoryId: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<VoucherListRes, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getVoucherList(userid, voucherCategoryId, voucherSubcategoryId)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }
    fun getVoucherDetail(
        userid: String?,
        voucher_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<voucherdatailRes, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getVoucherDetails(userid, voucher_id)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getTransactionToken(
        userid: String?,
        amount: Double,
        orderId: String,
        loadLis: LoaderListener?,
        listener: ApiListener<TransactionTokenRes, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getTransactionToken(
                    "https://tapfo.in/delta/paytm/initiate_transaction.php", userid, amount, orderId
                )
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getRechargeService(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<RechargeServiceRes, Any?>,
    ) {
        this.loadLis = loadLis
        //      loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getRechargeService(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getRechargeCircle(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<RechargeCircle, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getRechargeCircle(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getRechargeServiceOperator(
        userid: String?,
        rechargeservicestype_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<ServiceOperatorRes, Any?>,
    ) {
        this.loadLis = loadLis
          loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getRechargeServiceOperator(userid, rechargeservicestype_id)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }
    fun getStoriesData(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<StoriesResFile, Any?>,
    ) {
        this.loadLis = loadLis
        //        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getStoriesData(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getAllGames(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<Games, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getAllGames(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getGamezopCategoryData(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<GamezopCategoryData, Games>,
    ) {
        this.loadLis = loadLis
        //   loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getGamezopCategoryData(userid).body()
                    ?.let { listener.onResponse(it, loadLis) }
            }
        }
    }

    fun getGamezopTag(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<GamezopTag, Any?>,
    ) {
        this.loadLis = loadLis
        //      loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getGamezopTag(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

//    fun getGameFavList (
//        userid: String?,
//        loadLis: LoaderListener?,
//        listener: ApiListener<getGameFavList, Any?>,
//    ):LiveData<List<getGameFavList>>  {
//        this.loadLis = loadLis
//        loadLis?.showLoader()
//        viewModelScope.launch(setErrorHandler(loadLis)) {
//            withContext(coroutineContext) {
//                MyApi().getGameFavList(userid)
//                    .body()
//                    ?.let {
//                        listener.onResponse(it, loadLis)
//                    }
//            }
//        }
//    }

    fun getGameFavList(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<getGameFavList, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getGameFavList(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getFaqsModel(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<Faqsmodel, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getFAQs(userid)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getGamesFavUnFav(
        userId: String,
        game_id: String,
        favUnfav: String,
        loadLis: LoaderListener,
        listener: ApiListener<getFavUnFav, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getGameFavandUnFav(userId, game_id, favUnfav).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getaddToRecentData(
        userId: String,
        game_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<addGameToRecentList, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getaddGametoRecentList(userId, game_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getRecentDataList(
        userId: String,
        loadLis: LoaderListener,
        listener: ApiListener<GameRecentPlayList, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getGameRecentList(userId).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getShopCategory(
        userId: String,
        loadLis: LoaderListener,
        listener: ApiListener<ShopCategory, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getShopCategory(userId).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getShopSubCategory(
        userId: String,
        category_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<ShopSubCategory, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getShopSubCategory(userId, category_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getShopChildCategory(
        userId: String,
        category_id: String,
        sub_category_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<ShopChildCategory, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getShopChildCategory(userId, category_id, sub_category_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getShopProduct(
        userId: String,
        category_id: String,
        sub_category_id: String,
        child_category_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<ShopProductData, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getShopProduct(userId, category_id, sub_category_id, child_category_id)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getNews(
        userId: String,
        id: String,
        page: String,
        loadLis: LoaderListener,
        listener: ApiListener<getAllNewsdata, Any?>,
    ) {
        this.loadLis = loadLis

        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNews(userId, id, page).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getNewsCategories(
        userId: String,
        loadLis: LoaderListener,
        listener: ApiListener<getCategories, Any?>,
    ) {
        this.loadLis = loadLis
        //    loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNewsCategory(userId).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun OperatorFatchAPI(
        user_id: String?,
        number: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<getRechargeoperatorFeatch, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getRechargeOperatorFeatch(user_id, number).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getAllRechargePlans(
        user_id: String,
        number: String,
        operator_id: String,
        circle_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<getRechargePlans, Any?>,
    ) {
        this.loadLis = loadLis
        // loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getRechargePlans(user_id, number, operator_id, circle_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun rechargeprocess(
        user_id: String,
        recharge_type: String,
        number: String,
        operator_id: String,
        circle_id: String,
        amount: String,
        user_txn_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<RechargeDoneOrNotRes, Any?>,
    ) {
        this.loadLis = loadLis
        //   loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().rechargeprocess(user_id, recharge_type, number, operator_id, circle_id, amount,user_txn_id ).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getLeaderBoardData(
        user_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<leaderboardRes, Any?>,
    ) {
        this.loadLis = loadLis
        //     loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getLeaderBoardData(user_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getAllNotification(
        user_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<AllNotificationRes, Any?>,
    ) {
        this.loadLis = loadLis
        //  loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getAllNotification(user_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getInitiateTransaction(
        userid: String?,
        amount: Double,
        orderId: String,
        loadLis: LoaderListener?,
        listener: ApiListener<InitiateTransactionRes, Any?>,
    ) {
        this.loadLis = loadLis
//        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().get_initiate_transaction(/*"https://tapfo.in/delta/paytm/initiate_transaction.php",*/ userid, amount, orderId)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getTransactionProcess(
        user_id: String?,
        order_id: String,
        payment_mode: String,
        card_info: String,
        txnToken: String,
        loadLis: LoaderListener?,
        listener: ApiListener<TransactionProcessRes, Any?>,
    ) {
        this.loadLis = loadLis
//        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getTransactionProsses(/*"https://tapfo.in/delta/paytm/process_transaction.php",*/ user_id, order_id, payment_mode, card_info, txnToken)
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getTransactionStatus(
        user_id: String?,
        order_id: String,
        loadLis: LoaderListener?,
        listener: ApiListener<TransactionStatusRes, Any?>,
    ) {
        this.loadLis = loadLis
//        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getTransactionStatus(
                    /*"https://tapfo.in/delta/paytm/transaction_status.php",*/
                    user_id,
                    order_id
                )
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun addUserTransaction(
        userid: String?,
        amount: String,
        payment_amount: String,
        txn_id: String,
        pay_option: String,
        type: String,
        loadLis: LoaderListener?,
        listener: ApiListener<addUserTransactionRes, Any?>,
    ){
        this.loadLis =loadLis
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext){
                MyApi().addUserTransaction(userid,amount, payment_amount,txn_id,pay_option,type).body()?.let {
                    listener.onResponse(it,loadLis)
                }
            }

        }
    }
    fun getBusinessType(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<business_types, Any?>,
    ) {
        this.loadLis = loadLis
        //     loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessType(userid).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }

    fun getBusinesscategory(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BusinessCategory, Any?>,
    ) {
        this.loadLis = loadLis
        //     loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessCategory(userid).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }

    fun getBusinessSubCategory(
        userid: String?,
        categoryid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BusinessSubCategory, Any?>,
    ) {
        this.loadLis = loadLis
        //       loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessSubCategory(userid, categoryid).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }

    fun getSaveBusiness(
        user_id: String?,
        business_type_id: String?,
        business_category_id: String?,
        business_subcategory_id: String?,
        business_name: String?,
        pancard: String?,
        gst: String?,
        image: String?,
        logo: String?,
        description: String?,
        contacts: String?,
        email: String?,
        whatsapp_business: String?,
        established_year: String?,
        latitude: String?,
        longitude: String?,
        address: String?,
        floor: String?,
        area: String?,
        city: String?,
        pincode: String?,
        landmark: String?,
        temparary_close: String?,
        always_open: String?,
        sun_opening: String?,
        sun_closing: String?,
        mon_opening: String?,
        mon_closing: String?,
        tue_opening: String?,
        tue_closing: String?,
        wed_opening: String?,
        wed_closing: String?,
        thu_opening: String?,
        thu_closing: String?,
        fri_opening: String?,
        fri_closing: String?,
        sat_opening: String?,
        sat_closing: String?,
        editid: String?,
        business_tag_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<SaveBusinessRes, Any?>,
    ) {
        var imagePart: MultipartBody.Part? = null

        if (image.isNullOrEmpty().not()) {
            val imageFile = File(image!!)
            val requestFile: RequestBody =
                imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
            Log.d("ImageDataForUpload", "$imagePart")
        }
        var logoPart: MultipartBody.Part? = null

        if (logo.isNullOrEmpty().not()) {
            val logoFile = File(logo!!)
            val requestFile: RequestBody =
                logoFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            logoPart = MultipartBody.Part.createFormData("logo", logoFile.name, requestFile)
            Log.d("ImageDataForUpload", "$logoPart")
        }

        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().saveBusiness(
                    user_id!!.toRequestBody(),
                    business_type_id!!.toRequestBody(),
                    business_category_id!!.toRequestBody(),
                    business_subcategory_id!!.toRequestBody(),
                    business_name!!.toRequestBody(),
                    pancard!!.toRequestBody(),
                    gst!!.toRequestBody(),
                    imagePart,
                    logoPart,
                    description!!.toRequestBody(),
                    contacts!!.toRequestBody(),
                    email!!.toRequestBody(),
                    whatsapp_business!!.toRequestBody(),
                    established_year!!.toRequestBody(),
                    latitude!!.toRequestBody(),
                    longitude!!.toRequestBody(),
                    address!!.toRequestBody(),
                    floor!!.toRequestBody(),
                    area!!.toRequestBody(),
                    city!!.toRequestBody(),
                    pincode!!.toRequestBody(),
                    landmark!!.toRequestBody(),
                    temparary_close!!.toRequestBody(),
                    always_open!!.toRequestBody(),
                    sun_opening!!.toRequestBody(),
                    sun_closing!!.toRequestBody(),
                    mon_opening!!.toRequestBody(),
                    mon_closing!!.toRequestBody(),
                    tue_opening!!.toRequestBody(),
                    tue_closing!!.toRequestBody(),
                    wed_opening!!.toRequestBody(),
                    wed_closing!!.toRequestBody(),
                    thu_opening!!.toRequestBody(),
                    thu_closing!!.toRequestBody(),
                    fri_opening!!.toRequestBody(),
                    fri_closing!!.toRequestBody(),
                    sat_opening!!.toRequestBody(),
                    sat_closing!!.toRequestBody(),
                    editid!!.toRequestBody(),
                    business_tag_id!!.toRequestBody()
                ).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }

    fun getBusinessTag(
        userid: String?,
        business_category_id: String?,
        business_subcategory_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<businesstagRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessTag(userid, business_category_id, business_subcategory_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getbusinessDetails(
        userid: String?,
        business_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<getBusinessDetailRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessDetails(userid, business_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getbusinessListForBusinessPerson(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<getBusinessListResForBusinessPerson, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getbusinessListForBusinessPerson(userid).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun addRatingToBusiness(
        userid: String?,
        business_id: String?,
        rating: String?,
        review: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<AddBusinessRatingRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().AddBusinessRating(userid,business_id,rating,review).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun getBusinessListForUser(
        userid: String?,
        business_category_id: String?,
        business_subcategory_id: String?,
        business_name: String?,
        latitude: String?,
        longitude: String?,
        distance: String?,
        business_tag_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<SearchAllBusinessList, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessListForUser(userid,business_category_id,business_subcategory_id,business_name,latitude,longitude,distance,business_tag_id).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

}