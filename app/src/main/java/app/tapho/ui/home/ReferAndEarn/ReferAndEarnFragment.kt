package app.tapho.ui.home.ReferAndEarn

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import app.tapho.R
import app.tapho.databinding.FragmentReferAndEarnBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.DATA
import app.tapho.utils.toast
import app.tapho.utils.withSuffixAmount
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson


class ReferAndEarnFragment : BaseFragment<FragmentReferAndEarnBinding>() {


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
        _binding = FragmentReferAndEarnBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        getViewmodelData()
        referralAndEarn()

        return _binding?.root
    }


    private fun openBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.refer_and_earn_bottomsheet, null)
        dialog.setCancelable(true)
        //  val AllTransaction: TextView = view.findViewById(R.id.viewAllTransaction)
        val gotit: AppCompatButton = view.findViewById(R.id.gotit)
        gotit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun getViewmodelData() {
        val data = activity?.intent?.getStringExtra(DATA).toString()

        if (data.isNullOrEmpty().not()){

           val tcash = Gson().fromJson(data,TCashDasboardRes::class.java)
            tcash.let {
                    it!!.let {
                        val referdata : ArrayList<Txn> = ArrayList()
                        it.txn.forEach {
                            if ((it.type.equals("4"))){
                                referdata.add(it)
                            }
                        }
                        _binding!!.refcount.text =  referdata.size.toString()
                        var cashback = 0.0
                        referdata.forEach {
                            cashback= cashback+it.cashback.toDouble()
                        }
                        _binding!!.earnings.text = withSuffixAmount(cashback.toString())!!.dropLast(3)
                        _binding!!.seeAll.setOnClickListener {click->
                            ContainerActivity.openContainer(requireContext(),"OperAllMyReferProfile",it)
                        }
                        _binding!!.progress.visibility = View.GONE
                        _binding!!.mainLayout.visibility = View.VISIBLE
                    }
            }

        }else{


            viewModel.getTCashDashboard(getUserId(),TimePeriodDialog.getDate(1, -12),
                TimePeriodDialog.getCurrentDate(),"2",this,object : ApiListener<TCashDasboardRes,Any?>{
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        t.let {
                            it!!.let {
                                val referdata : ArrayList<Txn> = ArrayList()
                                it.txn.forEach {
                                    if ((it.type.equals("4"))){
                                        referdata.add(it)
                                    }
                                }
                                _binding!!.refcount.text =  referdata.size.toString()
                                var cashback = 0.0
                                referdata.forEach {
                                    cashback= cashback+it.cashback.toDouble()
                                }
                                _binding!!.earnings.text = withSuffixAmount(cashback.toString())!!.dropLast(3)
                                _binding!!.seeAll.setOnClickListener {click->
                                    ContainerActivity.openContainer(requireContext(),"OperAllMyReferProfile",it)
                                }
                                _binding!!.progress.visibility = View.GONE
                                _binding!!.mainLayout.visibility = View.VISIBLE
                            }

                        }
                    }
                })

        }

    }


    private fun referralAndEarn() {
        _binding!!.apply {
            invite.setOnClickListener {
                shareApp()
            }
            knowmore.setOnClickListener {
                openBottomSheet()
            }

            myrefer.text = getRefreelCode()
            codeCopied.setOnClickListener {
                val clipboard =requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label",myrefer.text.toString())
                clipboard.setPrimaryClip(clip)
                requireView().showShortSnack("Copied sucessfully")
            }
        }
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"Hey, I found a great app for you. Download Tapfo, Super app for super people. Earn extra cashback on your shopping from 900+ stores like flipkart, Amazon, Myntra, Swiggy & more. Get ₹8 signup bonus instantly from my Referral Code : *" + getRefreelCode() + "* \n" +
                    "\n" + "Don't forgot to put my Referral Code : *"+getRefreelCode()+"*"+
                    "\n\n\n\n Download the App now : https://tapfo.onelink.me/k6rU/qmgc2mid")
            type = "text/plain"
        }
        try {
            startActivity(Intent.createChooser(sendIntent, null))
        } catch (e: ActivityNotFoundException) {
            context?.toast("Unable to find market app")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ReferAndEarnFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}