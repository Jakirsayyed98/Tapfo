package app.tapho.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.NavSheet.Fragment_favorite_nav
import app.tapho.R
import app.tapho.databinding.ActivityContainerBinding
import app.tapho.ui.BuyVoucher.*
import app.tapho.ui.BuyVoucher.VoucherPayments.InitiatePaymentForVoucherFragment
import app.tapho.ui.Cabs.CabsParentPageFragment
import app.tapho.ui.Faqs.HelpAndSupportFragment
import app.tapho.ui.NewNotificationService.AllNotificationUI.NotificationFragment
import app.tapho.ui.News.NewsFragment.NewsHeadlineFragment
import app.tapho.ui.PaytmPaymentGateway.RechargeStatusPreviewFragment
import app.tapho.ui.PaytmPaymentGateway.SelectUPIAppsForPaymentFragment
import app.tapho.ui.PaytmPaymentGateway.TranscationProcessingPageFragment
import app.tapho.ui.PaytmPaymentGateway.paymentStatusScreenFragment
import app.tapho.ui.RechargeService.DTHRecharge.CustomerIdForDTHRechargeFragment
import app.tapho.ui.RechargeService.DTHRecharge.DTHAmountAndCheckFragment
import app.tapho.ui.RechargeService.DTHRecharge.DTHFragment
import app.tapho.ui.RechargeService.DTHRecharge.DTHRechargeFinalScreenFragment
import app.tapho.ui.RechargeService.Electricity.AccountNumberForBillFragment
import app.tapho.ui.RechargeService.Electricity.Electricity_billFragment
import app.tapho.ui.RechargeService.Electricity.EnterAmountForBillFragment
import app.tapho.ui.RechargeService.Electricity.FinalCheckForPaymentsFragment
import app.tapho.ui.RechargeService.MobileRechcrge.*
import app.tapho.ui.ScanAndPay.ScanAndPayIntroFragment
import app.tapho.ui.Stories.TapfoStoriesFragment
import app.tapho.ui.activecashback.ActiveCashbackScreenNew.CashbackOrderFragment
import app.tapho.ui.games.GameContainerFragment
import app.tapho.ui.help.NeedSupportFragment
import app.tapho.ui.help.SupportListFragment
import app.tapho.ui.home.*
import app.tapho.ui.home.GamesContainer.MostplayedFragment
import app.tapho.ui.home.GamesContainer.SeeAllNewlyaddedFragment
import app.tapho.ui.home.GamesContainer.SeeAllPopularFragment
import app.tapho.ui.home.GamesContainer.SeeAllTrandingGamesFragment
import app.tapho.ui.home.NewGame.*
import app.tapho.ui.home.OfferAndCoupon.NewOfferAndCouponFragment
import app.tapho.ui.home.OnlineStores.OnlineStoresFragment
import app.tapho.ui.home.ReferAndEarn.ReferAndEarnFragment
import app.tapho.ui.home.ReferAndEarn.referProfilelistFragment
import app.tapho.ui.localbizzUI.BusinessProfileFlow.localbizOnBoardingFragment
import app.tapho.ui.merchants.*
import app.tapho.ui.profile.BecomeAPartnerFragment
import app.tapho.ui.profile.CashbackHowItsWorksFragment
import app.tapho.ui.profile.HowOnlineCashbackWorksFragment
import app.tapho.ui.profile.ProfileBaseFragment
import app.tapho.ui.tcash.*
import app.tapho.ui.tcash.AddMoneyPopup.TopUpFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.AddBalanceStatusAndAmountFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.PaytmForWithOutWalletBalanceFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.PaytmWithoutWalletTranscationProcessingPageFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.StatusPreviewScreenFragment
import app.tapho.utils.*
import com.google.gson.Gson


class ContainerActivity : BaseActivity<ActivityContainerBinding>() {
    var type: String?=""
    companion object {
        var TitileText=""

        fun openContainer(
            context: Context?,
            type: String?,
            DeepLink: String?,
            orderID: String?,
            PackageName: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("PackageName", PackageName)
                putExtra("DeepLink",DeepLink)
                putExtra("orderID",orderID)
            })
        }
        fun openContainerForSupport(
            context: Context?,
            type: String?,
            data: Any?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }


        fun openContainerForPayment(
            context: Context?,
            type: String?,
            DeepLink: String?,
            orderID: String?,
            PackageName: String?,
            PaymentType: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("PackageName", PackageName)
                putExtra("DeepLink",DeepLink)
                putExtra("orderID",orderID)
                putExtra("PaymentType",PaymentType)
            })
        }
        fun openContainer(
            context: Context?,
            type: String?,
            txnToken: String?,
            orderID: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("txnToken",txnToken)
                putExtra("orderID",orderID)
            })
        }
        fun openContainerforPointScreen(
            context: Context?,
            type: String?,
            pointType: String?,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("pointType", pointType)
                putExtra(TITLE, title)

            })
        }

        fun openContainerForDTHRecharge(
            context: Context?,
            type: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
            })
        }


        fun openContainerForFinalRecharge(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
        fun openContainerForRecharge(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,

        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }


        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
        fun openContainerForTransaction(
            context: Context?,
            type: String?,
            Tcash_type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra("Tcash_type", Tcash_type)
                putExtra(TITLE, title)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
        fun openContainerforvoucher(
            context: Context?,
            type: String?,
            data: Any?,
            reddemtype: String?,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(TITLE, title)
                putExtra("reddemtype", reddemtype)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
        fun openContainerforPaymentStatus(
            context: Context?,
            type: String?,
            errorcode: String?,
            txn_Id: String,
            status: String?,
            statusType: String?,
            pspnamedata: String?,
            result: String?,
            data: Any?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("txn_Id", txn_Id)
                putExtra("errorcode", errorcode)
                putExtra("status", status)
                putExtra("statusType", statusType)
                putExtra("pspnamedata", pspnamedata)
                putExtra("result", result)

                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }

        fun openContainerforPaymentStatus(
            context: Context?,
            type: String?,
            status : String?,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(TITLE, title)
                putExtra("status", status)

            })
        }


        fun openContainer(
            context: Context?,
            type: String?,
            ServiceTypeID: String?,
            currentOperater: String?,
            CircleOperater: String?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(RECHARGE_SERVICETYPE_ID, ServiceTypeID)
                putExtra(CURRENTOPERATER, currentOperater)
                putExtra(CIRCLEOPERATER, CircleOperater)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
//                if(data is Bundle)
//                    putExtras(data)
//                else if (data != null)
//                    putExtra(DATA, Gson().toJson(data))
            })
        }

        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            gameid: String?,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                putExtra(GAME_ID, gameid)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }

        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
        ) {
            openContainer(context, type, data, false, "")
        }
        fun openContainerForVoucher(
            context: Context?,
            type: String?,
            Amount: String?,
            CouponApply: String?,
            cashback: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("Amount", Amount)
                putExtra("CouponApply", CouponApply)
                putExtra("cashback", cashback)

            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE
        init()
    }


    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {

            "InitiatePaymentForVoucher" -> {
                addFragment(InitiatePaymentForVoucherFragment.newInstance())
            }
            "CashbackOrderFragment" -> {
                addFragment(CashbackOrderFragment.newInstance())
            }

            "CabsParentPageFragment" -> {
                addFragment(CabsParentPageFragment.newInstance())
            }




            "NewHomeFragment" -> {
                addFragment(NewHomeFragment.newInstance())
            }
            "BecomeAPartnerFragment" -> {
                addFragment(BecomeAPartnerFragment.newInstance())
            }
            "AllTransactionFragment" -> {
                addFragment(AllTransactionFragment.newInstance())
            }
            "EndToEndEncriptionFragment" -> {
                addFragment(EndToEndEncriptionFragment.newInstance())
            }

            "TcashrewardsFragment" -> {
                addFragment(TcashrewardsFragment.newInstance())
            }
            "PopularVoucherFragment" -> {
                addFragment(PopularVoucherFragment.newInstance())
            }
            "FavouritesVoucherFragment" -> {
                addFragment(FavouritesVoucherFragment.newInstance())
            }
            "OpenGamesCategory" -> {
                addFragment(GameCategoyFragment.newInstance())
            }
            "OperAllMyReferProfile" -> {
                addFragment(referProfilelistFragment.newInstance())
            }
            "HowOnlineCashbackWorksFragment" -> {
                addFragment(HowOnlineCashbackWorksFragment.newInstance())
            }
            "OpenVoucherDetails" -> {
                addFragment(VoucherMoreDetailFragment.newInstance())
            }
            "OnlineStores" -> {
                addFragment(OnlineStoresFragment.newInstance())
            }
            "ScanAndPayIntroFragment" -> {
                addFragment(ScanAndPayIntroFragment.newInstance())
            }
               "PopularPagesFragment" -> {
                addFragment(PopularPagesFragment.newInstance())
            }

             "NeedSupportFragment" -> {
                addFragment(NeedSupportFragment.newInstance())
            }
               "OpenServiceList" -> {
                addFragment(SupportListFragment.newInstance())
            }
              "WelcomeBackScreen" -> {
                addFragment(welcomebackscreenFragment.newInstance())
            }
            getString(R.string.cashback_merchants) -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MerchantsAllListFragment.newInstance())
            }
            "MiniAppFragment" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MiniAppFragment.newInstance())
            }

            "GamesOneTimeSplash" -> {
                addFragment(GamesOneTimeSplashFragment.newInstance())
            }
            "AddMoneyTransactionStatuspreview" -> {
                addFragment(StatusPreviewScreenFragment.newInstance())
            }
            "SelectedCategoriesMiniAppsFragment" -> {
                addFragment(SelectedCategoriesMiniAppsFragment.newInstance())
            }
           "referandearnscreen" -> {
                addFragment(ReferAndEarnFragment.newInstance())
            }
             "AllPurchasedVouchers" -> {
                addFragment(PurchasedVouchersFragment.newInstance())
            }
            "VoucherPurchasedHistoryFragment" -> {
                addFragment(VoucherPurchasedHistoryFragment.newInstance())
            }
            "localbizOnBoarding" -> {
                addFragment(localbizOnBoardingFragment.newInstance())
            }
            "OpenVoucherList" -> {
                addFragment(SelectedVouchersListFragment.newInstance())
            }
            "ProceedForEnterAmount" -> {
                addFragment(EnterAmountForBillFragment.newInstance())
            }
            "OpenVoucherDetailForBuy" -> {
                addFragment(VoucherDetailFragment.newInstance())
            }
            "SelectRechargeOpretor" -> {
                addFragment(SelectRechargeOpretorFragment.newInstance())
            }
            "SelectRechargeCircle" -> {
                addFragment(SelectRechargeCircleFragment.newInstance())
            }
            "ProceedForBillAmountAndDetaileFeatch" -> {
                addFragment(FinalCheckForPaymentsFragment.newInstance())
            }

            "AddMoneyToWalletWithPaytm" -> {
                addFragment(PaytmForWithOutWalletBalanceFragment.newInstance())
            }
            "AddMoneyTransactionStatus" -> {
                addFragment(AddBalanceStatusAndAmountFragment.newInstance())
            }
            "Electricity_AccountNumber" -> {
                addFragment(AccountNumberForBillFragment.newInstance())
            }


            "FinalCheckForRecharge" -> {
                addFragment(BeforeRechargeAllDetailskFragment.newInstance())
            }

            "DTHRechargeFinalScreenFragment" -> {
                addFragment(DTHRechargeFinalScreenFragment.newInstance())
            }
            "ProceedForLastCheckDTHRecharge" -> {
                addFragment(DTHAmountAndCheckFragment.newInstance())
            }
            "Electricity_bill" -> {
                addFragment(Electricity_billFragment.newInstance())
            }
            "AddMoneyCardOffers" -> {
                addFragment(AddMoneyCardOffersFragment.newInstance())
            }

             "TransactionStatus" -> {
                addFragment(paymentStatusScreenFragment.newInstance())
            }
             "TransactionStatuspreviewpage" -> {
                addFragment(RechargeStatusPreviewFragment.newInstance())
            }
            "SelectpaymentMethods" -> {
                addFragment(SelectUPIAppsForPaymentFragment.newInstance())
            }

            "TapfoStoriesFragment" -> {
                addFragment(TapfoStoriesFragment.newInstance())
            }
             "GameNotification" -> {
                addFragment(GameNotificationFragment.newInstance())
            }
            "GamesFavouriteFragment" -> {
                addFragment(GamesFavouriteFragment.newInstance())
            }
             "ProceedForDTHRecharge" -> {
                addFragment(CustomerIdForDTHRechargeFragment.newInstance())
            }
            "CashbackHowItsWorksFragment" -> {
                addFragment(CashbackHowItsWorksFragment.newInstance())
            }
            "helpandsupport" -> {
                addFragment(HelpAndSupportFragment.newInstance())
            }
            "addtopup" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(TopUpFragment.newInstance())
            }

            "AllNotification" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NotificationFragment.newInstance())
            }

            "mobile_prepaid" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MobileRechargeFragment.newInstance())
            }
            "DTH_Recharge" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(DTHFragment.newInstance())
            }
            "News" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewsHeadlineFragment.newInstance())
            }

            "Deals & Coupons" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewOfferAndCouponFragment.newInstance())
            }

            "OffersFragment" -> {
                addFragment(OffersFragment.newInstance())
            }


            "cashbackcard" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(HomeCardToTcashPageFragment.newInstance())
            }

            "BuyGiftCard" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(BuyGiftCardFragment.newInstance())
            }


            "AvailableBalance" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(Fragment_wallet_history.newInstance())
            }

            "MiniAppFragmentNewCategories" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewMerchantCategoryFragment.newInstance())
            }

            "GameCategory" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GameContainerFragment.newInstance())
            }
            "GameSearch" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GameSearchFragment.newInstance())
            }

            "GamesHistory" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GamesHistoryFragment.newInstance())
            }

            "MostPlayed" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MostplayedFragment.newInstance())
            }
            "favouritesBtn" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(Fragment_favorite_nav.newInstance())
            }

            "TrandingGames" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SeeAllTrandingGamesFragment.newInstance())
            }

            "NewlyAdded" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SeeAllNewlyaddedFragment.newInstance())
            }
            "transactionHistory" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(Fragment_Tcash_HistoryData.newInstance())
            }
            "walletHistory" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(Fragment_wallet_history.newInstance())
            }
            "PopularGames" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SeeAllPopularFragment.newInstance())
            }

            "RechargeOrporaterAllPlans" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(AllPlansFragment.newInstance())
            }
            "Games" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GameHomeFragment.newInstance())
            }
            "TranscationProcessingPage" -> {
                addFragment(TranscationProcessingPageFragment.newInstance())
            }
            "PaytmWithoutWalletTranscationProcessingPage" -> {
                addFragment(PaytmWithoutWalletTranscationProcessingPageFragment.newInstance())
            }
            "SelectedOffersDetailFragment" -> {
                addFragment(SelectedOffersDetailFragment.newInstance())
            }

            getString(R.string.popular_merchants) -> {
                setCashbackMerchants()
            }

            ContainerType.MERCHANT_STORE.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(MerchantOfferFragment.newInstance())
            }
            ContainerType.MERCHANT_DETAIL.name -> {
                addFragment(MerchantOfferDetailFragment.newInstance())
            }
            ContainerType.PROFILE_EDIT.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(ProfileBaseFragment.newInstance())
            }

        }
    }

    private fun setCashbackMerchants() {
        addFragment(CashbackMerchantsFragment.newInstance(null, true, false))
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    }

    fun setTitlec(title: String?) {
//        binding.toolbar.tvTitle.text = title
    }
}