package app.tapho.ui

import android.R.attr.tag
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.tapho.NavSheet.Fragment_favorite_nav
import app.tapho.R
import app.tapho.databinding.ActivityContainerBinding
import app.tapho.ui.BuyVoucher.SelectedVouchersListFragment
import app.tapho.ui.BuyVoucher.VoucherCartFragment
import app.tapho.ui.BuyVoucher.VoucherDetailFragment
import app.tapho.ui.CustomeCategorySection.CustomeShopCategoryFragment
import app.tapho.ui.Faqs.HelpAndSupportFragment
import app.tapho.ui.LeaderShip.LeaderShipHomePageFragment
import app.tapho.ui.NewCashbackcard.NewCashbackcardFragment
import app.tapho.ui.NewNotificationService.AllNotificationUI.NotificationFragment
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.NewNotificationFragment
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.ReadNotificationFragment
import app.tapho.ui.News.NewsFragment.NewsHeadlineFragment
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
import app.tapho.ui.Stories.TapfoStoriesFragment
import app.tapho.ui.electro.ElectroOneTimeFragment
import app.tapho.ui.electro.FragmentsScreen.ElectroFragment
import app.tapho.ui.favourite.FavouriteFragment
import app.tapho.ui.games.GameContainerFragment
import app.tapho.ui.home.*
import app.tapho.ui.home.CabsContainer.CabsFragment
import app.tapho.ui.home.CompareCategories.InsuranceFragment
import app.tapho.ui.home.FinanceCategories.DonationFragment
import app.tapho.ui.home.GamesContainer.MostplayedFragment
import app.tapho.ui.home.GamesContainer.SeeAllNewlyaddedFragment
import app.tapho.ui.home.GamesContainer.SeeAllPopularFragment
import app.tapho.ui.home.GamesContainer.SeeAllTrandingGamesFragment
import app.tapho.ui.home.LocalOfferOnBoardingProcess.MiniEarnOnBoardingFragment
import app.tapho.ui.home.NewGame.GameHomeFragment
import app.tapho.ui.home.NewGame.GameNotificationFragment
import app.tapho.ui.home.NewGame.GamesOneTimeSplashFragment
import app.tapho.ui.home.OfferAndCoupon.NewOfferAndCouponFragment
import app.tapho.ui.home.Recommended.RecommendedFragment
import app.tapho.ui.home.SearchAndComare.SearchAndCompareFragment
import app.tapho.ui.home.ShopProduct.AllProdoctShopMarketPlaceFragment
import app.tapho.ui.home.TravelCategory.travelMainPageFragment
import app.tapho.ui.home.insidescreens.AllMiniStoresFragment
import app.tapho.ui.localbizzUI.BusinessProfileFlow.localbizOnBoardingFragment
import app.tapho.ui.merchants.*
import app.tapho.ui.profile.ProfileBaseFragment
import app.tapho.ui.tcash.*
import app.tapho.ui.tcash.AddMoneyPopup.TopUpFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.AddBalanceStatusAndAmountFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.PaytmForWithOutWalletBalanceFragment
import app.tapho.ui.tcash.DirectPaytmTransaction.PaytmWithoutWalletTranscationProcessingPageFragment
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
        fun openContainerforPaymentStatus(
            context: Context?,
            type: String?,
            errorcode: String?,
            txn_Id: String,
            status: String?,
            statusType: String?,
            pspnamedata: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("txn_Id", txn_Id)
                putExtra("errorcode", errorcode)
                putExtra("status", status)
                putExtra("statusType", statusType)
                putExtra("pspnamedata", pspnamedata)
            })
        }
        fun openContainerForNotification(
            context: Context?,
            type: String?,
            notificationType : String?,
            data: Any?,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(TITLE, title)
                putExtra(NOTIFICATIONTYPE, notificationType)
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

        fun openCustomeCategory(
            context: Context?,
            type: String?,
            category_id: String?,
            sub_category_id: String?,
            child_category_id: String?,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(TITLE, title)
                putExtra("category_id", category_id)
                putExtra("sub_category_id", sub_category_id)
                putExtra("child_category_id", child_category_id)
            })
        }

        fun openContainerDeals(
            context: Context?,
            type: String?,
            data: Any?,
            app_category_id: String?,
            miniapp_id: String?,
            title: String?,
            logo: String?,
        ) {
            context?.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("app_category_id", app_category_id)
                putExtra("miniapp_id", miniapp_id)
                putExtra("logo", logo)
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
        //statusBarColor(R.color.status_bar)

        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    //    statusBarTextWhite()

        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE
//        binding.toolbar.backIv.setOnClickListener { onBackPressed() }
        init()
    }


    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {
            getString(R.string.cashback_merchants) -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MerchantsAllListFragment.newInstance())
            }
            "MiniAppFragment" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MiniAppFragment.newInstance())
            }
            "Mini Apps" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(OnlyCashbackStoresFragment.newInstance())
            }
            "GamesOneTimeSplash" -> {
                addFragment(GamesOneTimeSplashFragment.newInstance())
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
            "WalletOnBoardingScreen" -> {
                addFragment(WalletOnBoardingFragment.newInstance())
            }
            "ProceedForBillAmountAndDetaileFeatch" -> {
                addFragment(FinalCheckForPaymentsFragment.newInstance())
            }
             "ElectroSplashFragment" -> {
                addFragment(ElectroOneTimeFragment.newInstance())
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
             "AllProductWithCategory" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(AllProdoctShopMarketPlaceFragment.newInstance())
            }
            "ElectroFragment" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(ElectroFragment.newInstance())
            }
              "FoCashDetails" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(FoCashDataFragment.newInstance())
            }
            "FinalCheckForRecharge" -> {
                addFragment(BeforeRechargeAllDetailskFragment.newInstance())
            }
            "OpenVoucherCart" -> {
                addFragment(VoucherCartFragment.newInstance())
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
            "SelectpaymentMethods" -> {
                addFragment(SelectUPIAppsForPaymentFragment.newInstance())
            }

            "TapfoStoriesFragment" -> {
                addFragment(TapfoStoriesFragment.newInstance())
            }
             "GameNotification" -> {
                addFragment(GameNotificationFragment.newInstance())
            }
             "ProceedForDTHRecharge" -> {
                addFragment(CustomerIdForDTHRechargeFragment.newInstance())
            }
              "HowItWork" -> {
                addFragment(HowItWorkFragment.newInstance())
            }
            "helpandsupport" -> {
                addFragment(HelpAndSupportFragment.newInstance())
            }
              "addtopup" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(TopUpFragment.newInstance())
            }
            ContainerType.NOTIFICATIONDATA.name -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewNotificationFragment.newInstance())
            }
            "FoCoinScreen" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(FoPointsScreenFragment.newInstance())
            }
            "AllNotification" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NotificationFragment.newInstance())
            }
            "Travel" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(travelMainPageFragment.newInstance())
            }
            "ReadNotification" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(ReadNotificationFragment.newInstance())
            }
            "mobile_prepaid" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MobileRechargeFragment.newInstance())
            }
            "DTH_Recharge" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(DTHFragment.newInstance())
            }

            "search" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MiniAppsearchfragmentFragment.newInstance())
            }
            "All Mini Apps" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(AllMiniAppsFragment.newInstance())
            }
            "AllCBStore" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(AllMerchantListFragment.newInstance())
            }
            "leadership" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(LeaderShipHomePageFragment.newInstance())
            }
            "NewCashbbackCard" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewCashbackcardFragment.newInstance())
            }
            "News" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewsHeadlineFragment.newInstance())
            }
            "compare&Shop" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SearchAndCompareFragment.newInstance())
            }
            "Deals & Coupons" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewOfferAndCouponFragment.newInstance())
//                addFragment(OffersFragment.newInstance())
            }

            "MerchantDeal" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MerchantDealFragment.newInstance())
            }
            "cashbackcard" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(HomeCardToTcashPageFragment.newInstance())
            }

            "BuyGiftCard" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(BuyGiftCardFragment.newInstance())
            }
            "Insurance" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(InsuranceFragment.newInstance())
            }
            "Donation" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(DonationFragment.newInstance())
            }

            "AvailableBalance" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(Fragment_wallet_history.newInstance())
            }
            "tcashpoint" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(TcashPointFragment.newInstance())
            }
            "Bills" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(BillsFragment.newInstance())
            }
            "MiniApps" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(AllMiniStoresFragment.newInstance())
            }
            "CustomeShopCategory" -> {
                TitileText= intent.getStringExtra(TITLE).toString()
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(CustomeShopCategoryFragment.newInstance())
            }
            "MiniAppFragmentNewCategories" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(NewMerchantCategoryFragment.newInstance())
            }
            "Cabs" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(CabsFragment.newInstance())
            }
            "GameCategory" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GameContainerFragment.newInstance())
            }
            "GameSearch" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GameSearchFragment.newInstance())
            }
            "MiniAppSearch" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SearchFragment.newInstance())
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
            "Shop & Compair" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SearchAndCompaireFragment.newInstance())
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
            "MiniEarnOnBoardingScreen" -> {
                addFragment(MiniEarnOnBoardingFragment.newInstance())
            }
            "TranscationProcessingPage" -> {
                addFragment(TranscationProcessingPageFragment.newInstance())
            }
            "PaytmWithoutWalletTranscationProcessingPage" -> {
                addFragment(PaytmWithoutWalletTranscationProcessingPageFragment.newInstance())
            }

            getString(R.string.popular_merchants) -> {
                setCashbackMerchants()
            }
            ContainerType.NOTIFICATION.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(NotificationsFragment.newInstance())
            }
            ContainerType.MERCHANT_STORE.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(MerchantOfferFragment.newInstance())
            }
            "RecommendedItem" -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(RecommendedFragment.newInstance())
            }
            ContainerType.MERCHANT_DETAIL.name -> {
                addFragment(MerchantOfferDetailFragment.newInstance())
            }
            ContainerType.PROFILE_EDIT.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(ProfileBaseFragment.newInstance())
            }
            ContainerType.EARNINGS.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(TCashDashboardFragment.newInstance(""))
            }
            ContainerType.MERCHANT_REPORTS.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(TCashDashboardFragment.newInstance("1"))
            }
            "Tcashdashboard" -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(TCashDashboardFragment.newInstance(""))
            }

            ContainerType.EARNINGS_REPORT.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(TCashDashboardFragment.newInstance(""))
            }
            ContainerType.FAVOURITE.name -> {
//                binding.toolbar.root.visibility = View.GONE
                addFragment(FavouriteFragment.newInstance())
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