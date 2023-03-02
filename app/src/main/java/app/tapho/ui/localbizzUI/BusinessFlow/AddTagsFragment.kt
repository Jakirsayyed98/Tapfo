package app.tapho.ui.localbizzUI.BusinessFlow

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColorStateList
import app.tapho.R
import app.tapho.databinding.FragmentAddTagsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessTags.Data
import app.tapho.ui.localbizzUI.Model.BusinessTags.businesstagRes
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.gson.Gson

class AddTagsFragment : BaseFragment<FragmentAddTagsBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTagsBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.toolbar.tvTitle.text="Listing Business"


        _binding!!.title.text = getString(R.string.what_are_the_services_offered_by,getSharedPreference().getString("BusinessName"))
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        setAllTextAndData()
        return _binding?.root
    }

    private fun setAllTextAndData() {
        viewModel.getBusinessTag(getUserId(),getSharedPreference().getString("category_id"),getSharedPreference().getString(""),this,object : ApiListener<businesstagRes,Any?>{
            override fun onSuccess(t: businesstagRes?, mess: String?) {
                t!!.let {
                    setDataInChip(it.data)
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        getSharedPreference().saveString("businesstags","")
    }
    private fun setDataInChip(data: List<Data>) {
        val chipData : ArrayList<Data> =ArrayList()
        for (name in data) {
            val chip = Chip(requireContext())
            chip.text = name.name.capitalize()
            chip.setCheckable(true)

            chip.setClickable(true)
            chip.setOnClickListener(View.OnClickListener {
                _binding!!.btnVerify.isSelected = true
                if (chip.isChecked){

                    chip.setChipIconTintResource(R.color.white)
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.black))
                    chip.setTextColor(getResources().getColorStateList(R.color.white))
                    chipData.add(name)
                }else{
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.grey_light2))
                    chip.setTextColor(getResources().getColorStateList(R.color.black))
                    chipData.remove(name)
                }

            })
            _binding!!.chipGroup.addView(chip)
        }


        var tags= ""
        var tagsIds= ""

        _binding!!.btnVerify.setOnClickListener {
            if (chipData.isNullOrEmpty().not()){
                chipData.forEach {
                    tags = tags+","+it.name
                }
                chipData.forEach {
                    tagsIds = tagsIds+","+it.id
                }
                getSharedPreference().saveString("businesstags",tags.drop(1))
                getSharedPreference().saveString("businesstagIds",tagsIds.drop(1))
                LocalbizContainerActivity.openContainer(requireContext(), "FormReviewFragment")
            }else{
                this.requireView().showShortSnack("Please select atleast 1 service")
            }

//           activity?. onBackPressedDispatcher?.onBackPressed()
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddTagsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}