package app.tapho.ui.localbizzUI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityLocalbizContainerBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.localbizzUI.BusinessFlow.*
import app.tapho.ui.localbizzUI.BusinessProfileFlow.*
import app.tapho.ui.localbizzUI.UserFlow.HomeScreenFragment
import app.tapho.ui.localbizzUI.UserFlow.ListofAllBusinessesFragment
import app.tapho.ui.localbizzUI.UserFlow.SelectSubCategoryFragment
import app.tapho.ui.localbizzUI.UserFlow.ShowSelectedBusinessDataFragment
import app.tapho.ui.localbizzUI.editBusinessProfile.EditBusinessFragment

import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import app.tapho.utils.SET_OLD_DATA
import app.tapho.utils.TITLE
import com.google.gson.Gson

class LocalbizContainerActivity : BaseActivity<ActivityLocalbizContainerBinding>() {

    companion object {
        fun openContainer(
            context: Context?,
            type: String?,
        ) {
            context?.startActivity(Intent(context, LocalbizContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)

            })
        }

        fun openContainer(
            context: Context?,
            type: String?,
            inputType : String?,
        ) {
            context?.startActivity(Intent(context, LocalbizContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("INPUT_TYPE", inputType)
            })
        }


        fun openContainerforEditBusiness(
            context: Context?,
            type: String?,
            business_id: String?,
        ) {
            context?.startActivity(Intent(context, LocalbizContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("business_id", business_id)
            })
        }
        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, LocalbizContainerActivity::class.java).apply {
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
            categoryID: String?,
            SubcategoryID: String?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, LocalbizContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                putExtra("categoryID",categoryID)
                putExtra("SubcategoryID",SubcategoryID)
            })
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalbizContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {
            "HomePage" -> {
                addFragment(HomeScreenFragment.newInstance())
            }
            "AddBusiness" -> {
                addFragment(AddbusinessFirstPageFragment.newInstance())
            }
            "AddInformation" -> {
                addFragment(AddInformationFragment.newInstance())
            }
            "business_type" -> {
                addFragment(businessTypeFragment.newInstance())
            }
            "businessServices" -> {
                addFragment(businessServiceFragment.newInstance())
            }
            "PinBizProfileFragment" -> {
                addFragment(PinBizProfileFragment.newInstance())
            }
             "category_name" -> {
                addFragment(AddcategoryFragment.newInstance())
            }
            "businessProfile" -> {
                addFragment(AddBusinessProfileDetailFragment.newInstance())
            }
            "AddBusinessAddressFragment" -> {
                addFragment(AddBusinessAddressFragment.newInstance())
            }
            "BusinessTimingFragment" -> {
                addFragment(BusinessTimingFragment.newInstance())
            }
            "AddbusinessName" -> {
                addFragment(AddBusinessNameFragment.newInstance())
            }
            "AddBusinessPancardFragment" -> {
                addFragment(AddBusinessPancardFragment.newInstance())
            }
            "businessGSTINNumber" -> {
                addFragment(AddBusinessGSTNFragment.newInstance())
            }
            "businessDiscription" -> {
                addFragment(businesDiscriptionFragment.newInstance())
            }
            "businessPhoneNumber" -> {
                addFragment(AddbusinessPhoneNumberFragment.newInstance())
            }
            "businessEmail" -> {
                addFragment(AddBusinessEmailFragment.newInstance())
            }
            "businessWebSite" -> {
                addFragment(AddBusinessWebsiteFragment.newInstance())
            }
            "businessWhatsappNumber" -> {
                addFragment(AddBusinessWhatsappNumberFragment.newInstance())
            }
            "BusinessEstbYear" -> {
                addFragment(AddBusinessEstablisedYearFragment.newInstance())
            }
            "FormReviewFragment" -> {
                addFragment(FormReviewFragment.newInstance())
            }
            "SendForTheReview" -> {
                addFragment(AllInformAtionSendForTheReviewFragment.newInstance())
            }
            "BusinessProfile" -> {
                addFragment(BusinessProfilePageFragment.newInstance())
            }
            "SubCategories" -> {
                addFragment(SelectSubCategoryFragment.newInstance())
            }
            "ShowAllBusiness" -> {
                addFragment(ListofAllBusinessesFragment.newInstance())
            }
            "ShowSelectedBusinessData" -> {
                addFragment(ShowSelectedBusinessDataFragment.newInstance())
            }
            "businessTag" -> {
                addFragment(AddTagsFragment.newInstance())
            }
            "EditBusinessFragment" -> {
                addFragment(EditBusinessFragment.newInstance())
            }
            "BusinessPersonRatingAndReviewFragment" -> {
                addFragment(SelectedBusinessReviewFragment.newInstance())
            }
            "GetBusinessCard" -> {
                addFragment(BizzCardFragment.newInstance())
            }
            "BusinessNotification" -> {
                addFragment(BusinessNotificationFragment.newInstance())
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}