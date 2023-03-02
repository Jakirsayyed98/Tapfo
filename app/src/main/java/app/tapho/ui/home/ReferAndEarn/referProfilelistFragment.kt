package app.tapho.ui.home.ReferAndEarn

import android.content.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentReferProfilelistBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.model.AppCategory
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.DATA
import app.tapho.utils.toast
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson


class referProfilelistFragment : BaseFragment<FragmentReferProfilelistBinding>(),RecyclerClickListener {

    private var mAdapter: referandEarnProfileAdapter? = null
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
        _binding = FragmentReferProfilelistBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE

        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        setLayoutOFProfile()

        getViewmodelData()

        referralAndEarn()

        return _binding?.root
    }

    private fun getViewmodelData() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),"2",this,object : ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        it!!.let {
                            val referdata : ArrayList<Txn> = ArrayList()
                            it.txn.forEach {
                                if ((it.type.equals("4"))){
                                    if (it.referral_user_detail.isNullOrEmpty().not()){
                                        referdata.add(it)
                                    }
                                }
                            }

                            _binding!!.refcount.text =  referdata.size.toString()
                            var cashback = 0.0
                            referdata.forEach {
                                cashback= cashback+it.cashback.toDouble()
                            }
                            _binding!!.earnings.text = withSuffixAmount(cashback.toString())!!.dropLast(3)
                           mAdapter!!.addAllItem(referdata)
                            _binding!!.progress.visibility = View.GONE
                            _binding!!.mainLayout.visibility = View.VISIBLE
                        }

                    }
                }
            })
    }


    private fun referralAndEarn() {
        _binding!!.apply {
            invite.setOnClickListener {
                shareApp()
            }

        }
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {

            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"Hey, I found a great app for you. Download Tapfo, Super app for super people. Earn extra cashback on your shopping from 900+ stores like flipkart, Amazon, Myntra, Swiggy & more. Get ₹30 signup bonus instantly from my Referral Code : *" + getRefreelCode() + "*\n\n\n Download the App now : https://tapfo.onelink.me/k6rU/qmgc2mid")
            type = "text/plain"

        }
        try {

            startActivity(Intent.createChooser(sendIntent, null))

        } catch (e: ActivityNotFoundException) {

            context?.toast("Unable to find market app")

        }
    }



    private fun setLayoutOFProfile() {
        mAdapter = referandEarnProfileAdapter(this)
        _binding!!.referprofiles.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            referProfilelistFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}