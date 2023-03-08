package app.tapho.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.VoucherBuyingApiRes
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.BuyVoucher.VoucherDetails.voucherdatailRes
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.Faqs.Model.Faqsmodel
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GameAddToRecent.addGameToRecentList
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GameFavandUnFav.getFavUnFav
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.GetGamezopTag.GamezopTag
import app.tapho.ui.help.model.SupportServiceListRes
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.model.HomeRes
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.RechargeService.ModelData.OperatorFatchModel.OpreatorfetchRes
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.RechargeService.ModelData.RechargeStatus.checkRechargeStatusRes
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
import app.tapho.ui.model.MinisRecentsRes
import app.tapho.ui.model.UserDetails.getUserDetailRes
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.encrypt
import app.tapho.utils.setErrorHandler
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RequestViewModel : ViewModel() {
    private var loadLis: LoaderListener? = null

    /*
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

     fun verifyOtp(
        userId: String,
        otp: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
//           loadLis.showLoader()
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().verifyOtp(userId, otp).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }



    */

    fun loginWithOtp(
        mobile: String,
        device_id: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("mobile_no",mobile)
            addProperty("device_id",device_id)
            addProperty("country_code","91")
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().loginOtpV2(encrypt(req.toString())).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun verifyOtp(
        mobile: String,
        device_id: String,
        otp: String,
        loadLis: LoaderListener,
        listener: ApiListener<LoginRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("country_code","91")
            addProperty("mobile_no",mobile)
            addProperty("device_id",device_id)
            addProperty("otp",otp)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().verifyOtpV2(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("referral_code",referral_code)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().verifyReferralCode(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("mobile_no",mobile)
            addProperty("country_code","91")
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().resendOtp(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userId)
            addProperty("passcode",passcode)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().setPasscode(encrypt(req.toString())).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun get_user_detail(
        userId: String,
        loadLis: LoaderListener,
        listener: ApiListener<getUserDetailRes, Any?>,
    ) {
        this.loadLis = loadLis
//             loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userId)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().get_user_detail(encrypt(req.toString())).body()
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

        val req = JsonObject().apply {
            addProperty("user_id",userId)
            addProperty("email",email)
            addProperty("name",name)
            addProperty("gender",gender)
            addProperty("dob",dob)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().updateProfile(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("passcode",passCode)
            addProperty("country_code",countryCode)
            addProperty("mobile_no",mobile)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().loginWithPasscode(encrypt(req.toString())).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun loginCheckPasscode(
        mobile: String?,
        device_id: String?,
        type: String,
        loadLis: LoaderListener,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis.showLoader()

        val req = JsonObject().apply {
            addProperty("device_id",device_id)
            addProperty("country_code","91")
            addProperty("mobile_no",mobile)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().loginCheckPasscodeV2(encrypt(req.toString())).body()
                    ?.let {
                        it.type = type
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }



//    private val returnList = MutableLiveData<Resource<HomeRes>>()
//    fun getHomeData1(
//        userid: String?,
//    ) {
////        this.loadLis = loadLis
//        returnList.postValue(Resource.loading(null))
//        try {
//            viewModelScope.launch(setErrorHandler(loadLis)) {
//                withContext(Dispatchers.IO) {
//                    val response = MyApi().getHomeData(userid)
//                    if (response.isSuccessful){
//                        returnList.postValue(Resource.success(response.body()))
//                    }else{
//                        returnList.postValue(Resource.error(response.errorBody().toString(), null))
//                    }
//
//                }
//            }
//
//        } catch (e: Exception) {
//            returnList.postValue(Resource.error(e.message, null))
//        }
//    }
//
//    fun getHomeData(): LiveData<Resource<HomeRes>> {
//        return returnList
//    }
// //

    fun getHomeData(
        type: String,
        userid: String?,
        loadLis: LoaderListener,
        listener: ApiListener<HomeRes, Any?>,
    ) {
        this.loadLis = loadLis
//        loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getHomeData(encrypt(req.toString())).body()
                    ?.let {

                        it.type = type
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getAppCategoryMiniApp(
        userid: String?,
        app_category_id: String?,
        app_sub_category_id: String?,
        loadLis: LoaderListener,
        listener: ApiListener<MiniAppRes, Any?>,
    ) {
        this.loadLis = loadLis
           loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("app_category_id",app_category_id)
            addProperty("app_sub_category_id",app_sub_category_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getAppCategoryMiniApp(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("mini_app_id",mini_app_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getMiniAppTCash(encrypt(req.toString())).body()
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
           loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("amount",amount)
            addProperty("cashback",cashback)
            addProperty("txn_id",txn_id)
            addProperty("pay_option",pay_option)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().addMoneyToWallet(encrypt(req.toString())).body()
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
           loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("mini_app_id",mini_app_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().miniAppFev(encrypt(req.toString())).body()
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
          loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("mini_app_id",mini_app_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().miniAppUnFev(encrypt(req.toString())).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun addMiniAppToRecent(
        user_id:String,
        mini_app_id: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BaseRes, Any?>
    ){
        val req= JsonObject().apply {
            addProperty("user_id",user_id)
            addProperty("app_category_mini_app_id",mini_app_id)
        }

        viewModelScope.launch(setErrorHandler(loadLis)){
            withContext(coroutineContext){
                MyApiV2().addMiniappRecently(encrypt(req.toString())).body()?.let {
                    listener.onResponse(it,loadLis)
                }
            }
        }

    }

    private val returnMiniRecents = MutableLiveData<Resource<MinisRecentsRes>>()
    fun getMiniRecentData(
        userid: String?,
    ) {
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        returnMiniRecents.postValue(Resource.loading(null))
        try {
            viewModelScope.launch(setErrorHandler(loadLis)) {
                withContext(Dispatchers.IO) {
                    val response = MyApiV2().getMiniappRecentlyList(encrypt(req.toString()))
                    if (response.isSuccessful){
                        returnMiniRecents.postValue(Resource.success(response.body()))
                    }else{
                        returnMiniRecents.postValue(Resource.error(response.errorBody().toString(), null))
                    }
                }
            }

        } catch (e: Exception) {
            returnMiniRecents.postValue(Resource.error(e.message, null))
        }
    }

    fun getMiniRecentData(): LiveData<Resource<MinisRecentsRes>> {
        return returnMiniRecents
    }


    fun getTCashDashboard(
        userid: String?,
        startDate: String?,
        endDate: String?,
        type: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<TCashDasboardRes, Any?>,
    ) {
        this.loadLis = loadLis
//            loadLis?.showLoader()

        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("start_date",startDate)
            addProperty("end_date",endDate)
            addProperty("type",type)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getTCashDashboard(encrypt(req.toString()).toString()).body()
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
          loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("offer_type",offer_type)
            addProperty("search","")
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getOffers(encrypt(req.toString()).toString()).body()
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
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("offer_type",offer_type)
            addProperty("app_category_id",app_category_id)
            addProperty("category_id",category_id)
            addProperty("type",type)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getOffersDetail(encrypt(req.toString()).toString())
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
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getSupportServiceList(encrypt(req.toString()))
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
         loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("subject",subject)
            addProperty("message",message)
            addProperty("service",service)
            addProperty("type",type)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().support(encrypt(req.toString()))
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    fun updateProfile2(
        userid: String?,
        email: String,
        name: String,
        gender: String,
        dob: String,
        loadLis: LoaderListener?,
        listener: ApiListener<LoginRes, Any?>,
    ) {
//        var imagePart: MultipartBody.Part? = null
//        if (image.isNullOrEmpty().not()) {
//            val imageFile = File(image)
//            val requestFile: RequestBody =
//                imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
//        }

        this.loadLis = loadLis
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid!!.toString())
            addProperty("email",email)
            addProperty("name",name)
            addProperty("gender",gender)
            addProperty("dob",dob)

        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().updateProfile2(encrypt(req.toString())).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }

    //   MyApi().updateProfile2(id?.toRequestBody(), email.toRequestBody(), name.toRequestBody(), gender.toRequestBody(), dob.toRequestBody(), imagePart,


    fun updateNotification(
        userid: String?,
        deviceToken: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<BaseRes, Any?>,
    ) {
        this.loadLis = loadLis
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("device_type","2")
            addProperty("device_token",deviceToken)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().updateNotification(encrypt(req.toString()))
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
//        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("search",search)
        }
        return viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().searchMiniApp(encrypt(req.toString())).body()?.let {
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getWalletSlab(encrypt(req.toString())).body()?.let {
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
         loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getVoucherCategory(encrypt(req.toString()))
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
      loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("voucher_category_id",voucherCategoryId)
            addProperty("voucher_sub_category_id",voucherSubcategoryId)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getVoucherList(encrypt(req.toString()))
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
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("voucher_id",voucher_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getVoucherDetails(encrypt(req.toString()))
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun BuyVouchersApi(
        userid: String?,
        user_txn_id: String?,
        voucher_detail: String?,
        total_price: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<VoucherBuyingApiRes, Any?>,
    ) {
        this.loadLis = loadLis
         loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("user_txn_id",user_txn_id)
            addProperty("voucher_detail",voucher_detail)
            addProperty("total_price",total_price)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().BuyVouchersApi(encrypt(req.toString()))
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
//       loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getRechargeService(encrypt(req.toString())).body()?.let {
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getRechargeCircle(encrypt(req.toString()))
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("rechargeservicestype_id",rechargeservicestype_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getRechargeServiceOperator(encrypt(req.toString())).body()?.let {
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
                loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getStoriesData(encrypt(req.toString()))
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getAllGames(
        userid: String?,
        category_id: String?,
        tag_id: String?,
        popular_trending: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<Games, Any?>,
    ) {
        this.loadLis = loadLis
//            loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("category_id",category_id)
            addProperty("tag_id",tag_id)
            addProperty("popular_trending",popular_trending)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getAllGames(encrypt(req.toString()))
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
           loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getGamezopCategoryData(encrypt(req.toString())).body()
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
              loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getGamezopTag(encrypt(req.toString()))
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


    fun getGameFavList(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<getGameFavList, Any?>,
    ) {
        this.loadLis = loadLis
            loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getGameFavList(encrypt(req.toString()))
                    .body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }

    private val returnFavList = MutableLiveData<Resource<getGameFavList>>()
    fun getGameFavList(
        userid: String?,
    ) {
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        returnFavList.postValue(Resource.loading(null))
        try {
            viewModelScope.launch(setErrorHandler(loadLis)) {
                withContext(Dispatchers.IO) {
                    val response = MyApiV2().getGameFavList(encrypt(req.toString()))
                    if (response.isSuccessful){
                        returnFavList.postValue(Resource.success(response.body()))
                    }else{
                        returnFavList.postValue(Resource.error(response.errorBody().toString(), null))
                    }
                }
            }

        } catch (e: Exception) {
            returnFavList.postValue(Resource.error(e.message, null))
        }
    }

    fun getGameFavList(): LiveData<Resource<getGameFavList>> {
        return returnFavList
    }

    fun getFaqsModel(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<Faqsmodel, Any?>,
    ) {
        this.loadLis = loadLis
          loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getFAQs(encrypt(req.toString()))
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

           loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userId)
            addProperty("game_id",game_id)
            addProperty("favUnfav",favUnfav)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getGameFavandUnFav(encrypt(req.toString())).body()
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

           loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userId)
            addProperty("game_id",game_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getaddGametoRecentList(encrypt(req.toString())).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }




    private val returnRecentList = MutableLiveData<Resource<GameRecentPlayList>>()
    fun getRecentDataList(
        userid: String?,
    ) {
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        returnRecentList.postValue(Resource.loading(null))
        try {
            viewModelScope.launch(setErrorHandler(loadLis)) {
                withContext(Dispatchers.IO) {
                    val response = MyApiV2().getGameRecentList(encrypt(req.toString()))
                    if (response.isSuccessful){
                        returnRecentList.postValue(Resource.success(response.body()))
                    }else{
                        returnRecentList.postValue(Resource.error(response.errorBody().toString(), null))
                    }
                }
            }

        } catch (e: Exception) {
            returnRecentList.postValue(Resource.error(e.message, null))
        }
    }

    fun getRecentDataList(): LiveData<Resource<GameRecentPlayList>> {
        return returnRecentList
    }


    fun getNews(
        userId: String,
        id: String,
        page: String,
        loadLis: LoaderListener,
        listener: ApiListener<getAllNewsdata, Any?>,
    ) {
        this.loadLis = loadLis
           loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userId)
            addProperty("id",id)
            addProperty("page",page)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNews(encrypt(req.toString())).body()
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
//            loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userId)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getNewsCategory(encrypt(req.toString())).body()
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
        listener: ApiListener<OpreatorfetchRes, Any?>,
    ) {
        this.loadLis = loadLis
//          loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",user_id)
            addProperty("number",number)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getRechargeOperatorFeatch(encrypt(req.toString())).body()
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
//         loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",user_id)
            addProperty("number",number)
            addProperty("operator_id",operator_id)
            addProperty("circle_id",circle_id)
        }


        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getRechargePlans(encrypt(req.toString())).body()
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
//           loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",user_id)
            addProperty("recharge_type",recharge_type)
            addProperty("number",number)
            addProperty("operator_id",operator_id)
            addProperty("circle_id",circle_id)
            addProperty("amount",amount)
            addProperty("user_txn_id",user_txn_id)
        }

        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().rechargeprocess(encrypt(req.toString())).body()
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
//          loadLis.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",user_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getAllNotification(encrypt(req.toString())).body()
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
        listener: ApiListener<InitiateTransactionRes2, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("amount",amount.toString())
            addProperty("order_id",orderId)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().get_initiate_transaction(encrypt(req.toString())).body()?.let {
                    listener.onResponse(it, loadLis)
                }
            }
        }
    }


    fun getTransactionProcess(
        user_id: String,
        order_id: String,
        payment_mode: String,
        card_info: String,
        txnToken: String,
        loadLis: LoaderListener?,
        listener: ApiListener<TransactionProcessRes, Any?>,
    ) {
        this.loadLis = loadLis
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",user_id)
            addProperty("order_id",order_id)
            addProperty("payment_mode",payment_mode)
            addProperty("card_info",card_info)
            addProperty("txnToken",txnToken)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getTransactionProsses(encrypt(req.toString())).body()?.let {
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
        loadLis?.showLoader()
        val req = JsonObject().apply {
            addProperty("user_id",user_id)
            addProperty("order_id",order_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApiV2().getTransactionStatus(encrypt(req.toString()))
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("amount",amount)
            addProperty("payment_amount",payment_amount)
            addProperty("txn_id",txn_id)
            addProperty("pay_option",pay_option)
            addProperty("type",type)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext){
                MyApiV2().addUserTransaction(encrypt(req.toString())).body()?.let {
                    listener.onResponse(it,loadLis)
                }
            }

        }
    }

    fun checkRechargeStatus(
        userid: String?,
        loadLis: LoaderListener?,
        listener: ApiListener<checkRechargeStatusRes, Any?>,
    ){
        this.loadLis =loadLis
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext){
                MyApiV2().checkRechargeStatus(encrypt(req.toString())).body()?.let {
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessType(encrypt(req.toString())).body()?.let {
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessCategory(encrypt(req.toString())).body()?.let {
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("category_id",categoryid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessSubCategory(encrypt(req.toString())).body()?.let {
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
        }
        var logoPart: MultipartBody.Part? = null

        if (logo.isNullOrEmpty().not()) {
            val logoFile = File(logo!!)
            val requestFile: RequestBody =
                logoFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            logoPart = MultipartBody.Part.createFormData("logo", logoFile.name, requestFile)

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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("business_category_id",business_category_id)
            addProperty("business_subcategory_id",business_subcategory_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessTag(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("business_id",business_id)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessDetails(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getbusinessListForBusinessPerson(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("business_id",business_id)
            addProperty("rating",rating)
            addProperty("review",review)
        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().AddBusinessRating(encrypt(req.toString())).body()
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
        val req = JsonObject().apply {
            addProperty("user_id",userid)
            addProperty("business_category_id",business_category_id)
            addProperty("business_subcategory_id",business_subcategory_id)
            addProperty("business_name",business_name)
            addProperty("latitude",latitude)
            addProperty("longitude",longitude)
            addProperty("distance",distance)
            addProperty("business_tag_id",business_tag_id)

        }
        viewModelScope.launch(setErrorHandler(loadLis)) {
            withContext(coroutineContext) {
                MyApi().getBusinessListForUser(encrypt(req.toString())).body()
                    ?.let {
                        listener.onResponse(it, loadLis)
                    }
            }
        }
    }


}