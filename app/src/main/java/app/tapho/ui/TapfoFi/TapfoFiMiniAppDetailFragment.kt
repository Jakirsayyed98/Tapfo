package app.tapho.ui.TapfoFi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTapfoFiMiniAppDetailBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.TapfoFi.Adapter.TapfoFiNotesAdapter
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FiCategoriesMiniAppsRes
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FinMiniApp
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FinSubCategory
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.finminiapp_detail
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.gson.Gson


class TapfoFiMiniAppDetailFragment : BaseFragment<FragmentTapfoFiMiniAppDetailBinding>() {


    var subCategory : ArrayList<FinSubCategory>? = null
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
        _binding = FragmentTapfoFiMiniAppDetailBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()




        val data = activity?.intent?.getStringExtra(DATA)
        _binding!!.title.text = activity?.intent?.getStringExtra(TITLE).toString()
        if (data.isNullOrEmpty().not()){
            Gson().fromJson(data, FinMiniApp::class.java).let {
                setlayoutData(it)
            }
        }

        val data1 = activity?.intent?.getStringExtra(DATA1)
        if (!data1.isNullOrEmpty()){
            Gson().fromJson(data1,FinSubCategory::class.java).let {
                _binding!!.subCategory.text = it.name
            }
        }else{
            _binding!!.subCategory.visibility = View.GONE
        }
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        return _binding?.root
    }

    private fun setlayoutData(it: FinMiniApp?) {
        _binding!!.apply {
            progress.visibility = View.GONE
            if(it!!.card.isNullOrEmpty().not()){
                Glide.with(requireContext()).load(it.card).into(card)
            }else{
                card.visibility = View.GONE
            }
            if(it.image.isNullOrEmpty().not()){
                Glide.with(requireContext()).load(it.image).circleCrop().into(image)
            }else{
                image.visibility = View.GONE
            }

            if (it.k1.isNullOrEmpty()){
                k1Layout.visibility = View.GONE
            }

            if (it.fin_merchant_payout.isNullOrEmpty().not()) {
                binding.cashback.text = withSuffixAmount(it.fin_merchant_payout.get(0).cashback)
            } else {
                binding.cashbacklayout.visibility = View.GONE
            }

            if (it.k2.isNullOrEmpty()){
                k2Layout.visibility = View.GONE
            }
            nameTv.text = it.name
            k1.text = withSuffixAmount(it.k1)
            k2.text = withSuffixAmount(it.k2)
            if (it.fin_merchant_payout.isNullOrEmpty().not()){
                binding.cashback.text =withSuffixAmount(it.fin_merchant_payout.get(0).cashback)
//                popular.visibility = if (it.fin_merchant_payout.get(0).popular_cashback.equals("1")) View.VISIBLE else View.GONE
            }


            Apply.setOnClickListener { click->
                requireContext().setOnCustomeCrome(it.url)
            }

            val TapfoFiNotesAdapter = TapfoFiNotesAdapter<finminiapp_detail>(object :
                RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                }
            })
            notes.apply {
                layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                adapter = TapfoFiNotesAdapter
            }

            if (it.fin_mini_app_detail.isNullOrEmpty().not()){
                TapfoFiNotesAdapter.addAllItem(it.fin_mini_app_detail  )
            }


        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TapfoFiMiniAppDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}