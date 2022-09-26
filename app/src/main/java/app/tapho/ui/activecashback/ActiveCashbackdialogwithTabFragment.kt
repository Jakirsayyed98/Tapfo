package app.tapho.ui.activecashback

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.FragmentActiveCashbackdialogwithTabBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.ContainerActivity
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.News.NewsFragment.NewsHeadlineFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.activecashback.ActiveCashbackScreenNew.ActiveCashbackActivity
import app.tapho.ui.activecashback.ActiveCashbackScreenNew.CashbackOrderFragment
import app.tapho.ui.home.*
import app.tapho.ui.home.NewGame.GameHomeFragment
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.merchants.HowItWorkFragment
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TCashDashboardFragment
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.DATA
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson

class ActiveCashbackdialogwithTabFragment : DialogFragment(), ApiListener<MiniAppRes1, Any?> {
    private var _binding: FragmentActiveCashbackdialogwithTabBinding? = null
    var textCashback: String? = null
    var tcash: WebTCashRes? = null
    var res: WebTCashRes? = null
    var tcash2: TCashDasboardRes? = null
    var miniApp: MiniAppRes1? = null
    var miniappID:String?=null
    val window: FragmentActivity? = this.activity
    var miniappName=""
    private var madapter: PagerFragmentAdapter? = null
    var binding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)

    }


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        _binding = FragmentActiveCashbackdialogwithTabBinding.inflate(layoutInflater, container, false)

        _binding!!.homeTab.isSelected = true
        binding.cashbackscreen.visibility = View.VISIBLE
        binding.container.visibility = View.GONE
        setTab()
        tabClicked(_binding!!.homeTab)
        selectUnselectTab()
        //  setCashbackMerchantsPager()
        dialog?.window?.statusBarColor= Color.BLACK

        _binding!!.backBtn.setOnClickListener {
            activity?.finish()
        }
        //   (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        return _binding?.root
    }

    private fun setTab() {

        binding.homeTab.setOnClickListener {
//            binding.cashbackscreen.visibility = View.VISIBLE
//            binding.container.visibility = View.GONE
            tabClicked(it)
        }
        binding.infobtn.setOnClickListener {
//            binding.cashbackscreen.visibility = View.GONE
//            binding.container.visibility = View.VISIBLE
            tabClicked(it)
        }
        binding.offerandCoupons.setOnClickListener {
//            binding.cashbackscreen.visibility = View.GONE
//            binding.container.visibility = View.VISIBLE
            tabClicked(it)
        }
        binding.history.setOnClickListener {
//            binding.cashbackscreen.visibility = View.GONE
//            binding.container.visibility = View.VISIBLE
            tabClicked(it)
        }

    }

    fun tabClicked(view: View) {
        //      binding.homeTab.isSelected = false
        binding.infobtn.isSelected = false
        binding.offerandCoupons.isSelected = false
        binding.history.isSelected = false

        view.isSelected = true
        if (view.id == R.id.homeTab)
            binding.homeIv.isSelected = true
        when (view) {

            binding.infobtn -> addFragment(HowItWorkFragment.newInstance())
            binding.offerandCoupons -> addFragment(MerchantDealFragment.newInstance())
            binding.history -> addFragment(CashbackOrderFragment.newInstance(res,true))

        }
    }

    private fun addFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment)
            ?.addToBackStack("BACK")?.commit()
    }


    private fun selectUnselectTab() {

    }

    companion object {
        var merchantAppName: String? = null
        fun newInstance(t: WebTCashRes?, data: TCashDasboardRes?, showBack: Boolean): ActiveCashbackdialogwithTabFragment {
            val args = Bundle().apply {
                putString(DATA, Gson().toJson(t))
                putString("DATA", Gson().toJson(data))

                // putBoolean("SHOW_BACK", showBack)
            }
            val fragment = ActiveCashbackdialogwithTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        dialog.dismiss()
        activity?.finish()
        super.onCancel(dialog)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()

        setListener()

        _binding?.continueLi?.setOnClickListener {
            activate()
        }

    }


    private fun getData() {
        Gson().fromJson(arguments?.getString(DATA), WebTCashRes::class.java)?.let {
            miniappName = it.mini_app!!.get(0).name.toString()
            setData(it)
        }
        Gson().fromJson(arguments?.getString("DATA"), TCashDasboardRes::class.java)?.let { Data->
            Tcashdata(Data)
        }

    }

    private fun Tcashdata(data: TCashDasboardRes) {
        var tcashdatalist : ArrayList<TCashDashboardData> = ArrayList()
        tcash2= data
        var total=0.0
        var verified=0.0
        var pending=0.0
        data.data?.forEach {tcashdata->
            if (tcashdata.offer_name == miniappName){
                tcashdatalist.add(tcashdata)
            }
        }
        tcashdatalist.forEach {
            if (it.status!!.uppercase().equals("PENDING")){
                pending = pending+it.user_commision!!.toDouble()
            }else  if (it.status!!.uppercase().equals("VERIFIED")){
                verified = verified+it.user_commision!!.toDouble()
            }else  if (it.status!!.uppercase().equals("VALIDATED")){
                verified = verified+it.user_commision!!.toDouble()
            }

        }
        _binding!!.verifiedCashback.text = withSuffixAmount(verified.toString())
        _binding!!.pendingcashback.text = withSuffixAmount(pending.toString())

    }


    @SuppressLint("SetTextI18n")
    private fun setData(it: WebTCashRes) {


        tcash = it
        res = it
        val app_category_id=it.mini_app!!.get(0).app_category_id
        val miniAppId=it.mini_app!!.get(0).id
        var url = "https://tapfo.in/delta/public/storage/all_images/"
        miniappID = it.mini_app!!.get(0).id
        textCashback = it.data.get(0).cashback.toString()
        _binding?.cashback?.text = textCashback
        _binding?.cashback1?.text = "to activate " + textCashback
        _binding?.storename?.text = it.mini_app?.get(0)?.name
        _binding?.termsandcondition?.text = it.mini_app?.get(0)?.terms
        _binding?.nameTv2?.text = getString(R.string.activate, it.mini_app?.get(0)?.name)
        _binding?.howtoearn?.text = getString(R.string.how_to_earn_extra_cashback_from_kapiva, it.mini_app?.get(0)?.name)
        _binding?.term3?.text = getString(R.string.after_return_refund, it.mini_app?.get(0)?.name)
        _binding?.term1?.text = getString(R.string.open_hammer_link_, it.mini_app?.get(0)?.name,it.mini_app?.get(0)?.cashback.toString().replace("cashback",""))
        _binding?.term2?.text = getString(R.string.hammer_takes_15_mins_to_validate_, it.mini_app?.get(0)?.name,it.mini_app?.get(0)?.report)


        _binding?.pepoleLove?.text = getString(R.string._0_people_love_this, it.mini_app?.get(0)?.total_favourite_count)
        merchantAppName = it.mini_app?.get(0)?.name
        if (it.mini_app!!.get(0).tcash=="0"){
            _binding?.activeted!!.text = "deactiveted"
        }else{
            _binding?.activeted!!.text = "activeted"
        }

        _binding!!.howitwork.setOnClickListener {
            ContainerActivity.openContainerDeals(context,"HowItWork","",app_category_id,miniAppId,merchantAppName,"")
        }
//        _binding!!.helpandsupport.setOnClickListener {
//            ContainerActivity.openContainerDeals(context,"helpandsupport","",app_category_id,miniAppId,merchantAppName,"")
//        }

        it.data[0].cashback?.let { it1 -> /*setReward(it1)*/ }
        Glide.with(this).load(url+it.data.get(0).image).centerCrop().into(_binding!!.banner)
//           Glide.with(this).load(R.drawable.cardbackgroundoffer).centerCrop().into(_binding!!.background)
//

        _binding?.productIv?.let { it1 ->
            Glide.with(this).asBitmap().load(it.mini_app?.get(0)?.image).centerCrop()
                .into(object : BitmapImageViewTarget(it1) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        super.onResourceReady(resource, transition)
                        setColor(resource)
                    }
                })
        }
        // sendData(it)
    }

//30/03/2022

    private fun sendData(res: WebTCashRes) {
        //  val navHostFragment = (childFragmentManager.findFragmentById(R.id.cashbackNavigationView) as NavHostFragment)
//
//        _binding?.backBtn?.setOnClickListener {
//            back(navHostFragment.navController)
//        }
//
//
//        val startFrag = navHostFragment.childFragmentManager.fragments[0]
//        if (startFrag is OrdersFragment) {
//            startFrag.setData1(res, 300)
//
//
//        }
    }


    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    private fun setColor(bitmap: Bitmap) {
        val defColor = ContextCompat.getColor(requireContext(), R.color.black)

        /*
//        val lightvibrant=createPaletteSync(bitmap).getLightVibrantColor(defColor)
//        val vibrant = createPaletteSync(bitmap).getVibrantColor(defColor)
//        val darkvibrant=createPaletteSync(bitmap).getDarkVibrantColor(defColor)
//        val lightMuted=createPaletteSync(bitmap).getLightMutedColor(defColor)
//        val muted=createPaletteSync(bitmap).getMutedColor(defColor)
//        val darkMuted=createPaletteSync(bitmap).getDarkMutedColor(defColor)

    _binding?.image1?.setBackgroundColor(lightvibrant)
//        _binding?.image2?.setBackgroundColor(vibrant)
//        _binding?.image3?.setBackgroundColor(darkvibrant)
//        _binding?.image4?.setBackgroundColor(lightMuted)
//        _binding?.image5?.setBackgroundColor(muted)
//        _binding?.image5?.setBackgroundColor(darkMuted)
*/

        createPaletteSync(bitmap).vibrantSwatch?.rgb?.let {
            setColor(it)

        }
    }

    private fun setColor(color: Int) {

        //  _binding?.mainRe?.setBackgroundColor(color)
        //     (_binding?.continueLi?.background as GradientDrawable).setColor(color)
        //     dialog?.window?.statusBarColor=color
    }

    private fun back(navController: NavController) {
        if (navController.popBackStack().not()) {
            activity?.finish()

        }
    }

    private fun setListener() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            "ACTIVATE",
            viewLifecycleOwner,
            { requestKey, result ->
                if (requestKey == "ACTIVATE" && result.getBoolean("ACTIVATE")) {
                    activate()
                }
            }
        )
    }


    private fun activate() {
        if (activity is WebViewActivity) {
            (activity as WebViewActivity).initWebView()
        }
        dismiss()

    }

    override fun onSuccess(t: MiniAppRes1?, mess: String?) {

    }
}
