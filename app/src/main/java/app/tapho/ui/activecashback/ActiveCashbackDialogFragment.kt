package app.tapho.ui.activecashback

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.FragmentActiveCashbackDialogBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.BaseRes
import app.tapho.network.RequestViewModel
import app.tapho.ui.ContainerActivity
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.WebViewActivity
import app.tapho.ui.activecashback.ActiveCashbackScreenNew.ActiveCashbackActivity
import app.tapho.ui.activecashback.ActiveCashbackScreenNew.CashbackOrderFragment
import app.tapho.ui.activecashback.ActiveCashbackScreenNew.NewOrderFragment
import app.tapho.ui.activecashback.adapter.OrdersAdapter
import app.tapho.ui.home.MerchantDealFragment
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.merchants.HowItWorkFragment
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_active_cashback_dialog.view.*

class ActiveCashbackDialogFragment : DialogFragment(),ApiListener<MiniAppRes1,Any?>,
    LoaderListener {
    private var _binding: FragmentActiveCashbackDialogBinding? = null

    lateinit var viewModel: RequestViewModel
    var textCashback: String? = null
    var tcash: WebTCashRes? = null
    var res: WebTCashRes? = null
    var tcash2: TCashDasboardRes? = null
    var miniApp: MiniAppRes1? = null
    var miniappID:String?=null
    val window: FragmentActivity? = this.activity
    var miniappName=""
    private var madapter: PagerFragmentAdapter? = null

    private var mAdapter: TCashbackAdapter? = null


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
        _binding = FragmentActiveCashbackDialogBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
        setRecyclerLayout()
        dialog?.window?.statusBarColor=Color.BLACK
        _binding!!.backBtn.setOnClickListener {
            activity?.finish()
        }

//        _binding!!.infobtn -> addFragment(HowItWorkFragment.newInstance())
//            _binding!!.offerandCoupons -> addFragment(MerchantDealFragment.newInstance())
//            _binding!!.history ->addFragment(CashbackOrderFragment.newInstance(res,true))

        return _binding?.root
    }

    private fun setRecyclerLayout() {
        mAdapter = TCashbackAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "detail" && data is TCashDashboardData) {
//            startActivity(Intent(context, TCashbackDetailActivity::class.java).apply {
                    startActivity(Intent(context, tcashback_detail_Activity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(DATA, Gson().toJson(data))
                    })
                }
            }

        })
//        _binding!!.recyclerCashback.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//            adapter = mAdapter
//        }
    }


    companion object {
        var merchantAppName: String? = null
        fun newInstance(t: WebTCashRes?, data: TCashDasboardRes?, showBack: Boolean): ActiveCashbackDialogFragment {
            val args = Bundle().apply {
                putString(DATA, Gson().toJson(t))
                putString("DATA", Gson().toJson(data))

                // putBoolean("SHOW_BACK", showBack)
            }
            val fragment = ActiveCashbackDialogFragment()
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
        Gson().fromJson(arguments?.getString("DATA"), TCashDasboardRes::class.java)?.let {Data->
            Tcashdata(Data)
        }

    }

    private fun Tcashdata(data: TCashDasboardRes) {
        var tcashdatalist : ArrayList<TCashDashboardData> = ArrayList()
        var tcashlist : ArrayList<TCashDashboardData> = ArrayList()

        tcash2= data
        var verified=0.0
        var pending=0.0
        data.data!!.forEachIndexed { index, d ->
            if (d.offer_name == res?.mini_app?.get(0)?.name) {
                tcashlist.add(d)
            }
        }
        mAdapter?.addAllItem(if (tcashlist.size>1){ tcashlist.subList(0,1) }else tcashlist)
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

        _binding!!.verifiedAmount.text = withSuffixAmount(verified.toString())
        _binding!!.pendingcashback.text =withSuffixAmount(pending.toString())!!.replaceAfter(".","").replace(".","")





    }
    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(requireContext())
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

        _binding?.cashback?.text =  getString(R.string.upto_9_cashback,textCashback!!.replace("Upto ","").replace("Cashback",""))


        _binding?.cashback1?.text = "to activate " + textCashback




        _binding?.storename?.text = it.mini_app?.get(0)?.name
        _binding!!.termsAndCondition.text = getString(R.string.by_proceeding_to,it.mini_app?.get(0)?.name)
//        _binding?.standardname?.text = it.mini_app?.get(0)?.name
//        _binding?.label?.text = it.mini_app?.get(0)?.punchline
        _binding?.discription?.text = it.mini_app?.get(0)?.description
        _binding?.nameTv2?.text = getString(R.string.activate, it.mini_app?.get(0)?.name)
        _binding?.nameTv3?.text = it.mini_app?.get(0)?.name
        _binding?.report?.text = it.data?.get(0)?.report
        /*
//        _binding?.emptyTv?.text = getString(R.string.make_your_first_move_to_start_earnings_extra_on_s, it.mini_app?.get(0)?.name)
//        _binding?.earningName1?.text = getString(R.string.swiggy_earning, it.mini_app?.get(0)?.name)
//        _binding?.pendingform?.text = getString(R.string.pendingform, it.mini_app?.get(0)?.name)
//        _binding?.howtoearn?.text = getString(R.string.how_to_earn_extra_cashback_from_kapiva, it.mini_app?.get(0)?.name)
//        _binding?.term3?.text = getString(R.string.after_return_refund, it.mini_app?.get(0)?.name)
//        _binding?.term1?.text = getString(R.string.open_hammer_link_, it.mini_app?.get(0)?.name,textCashback )
//        _binding?.term2?.text = getString(R.string.hammer_takes_15_mins_to_validate_, it.mini_app?.get(0)?.name,it.data.get(0).report)

         */
        ActiveCashbackdialogwithTabFragment.merchantAppName = it.mini_app?.get(0)?.name


        _binding!!.info.setOnClickListener {
            ContainerActivity.openContainerDeals(context,"HowItWork","",app_category_id,miniAppId, ActiveCashbackdialogwithTabFragment.merchantAppName,"")
        }
//        _binding!!.helpandsupport.setOnClickListener {
//            ContainerActivity.openContainerDeals(context,"helpandsupport","",app_category_id,miniAppId,merchantAppName,"")
//        }

        it.data[0].cashback?.let { it1 -> /*setReward(it1)*/ }

        _binding!!.alltransaction.setOnClickListener { click->
            ActiveCashbackActivity.openWebView(requireContext(),it.mini_app!!.get(0).url,miniappID)
        }
//        _binding!!.cashbackcard.setOnClickListener { click->
//            ActiveCashbackActivity.openWebView(requireContext(),it.mini_app!!.get(0).url,miniappID)
//        }
        Glide.with(this).asBitmap().load(it!!.mini_app!!.get(0).logo).into(_binding!!.logo)
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
      //      setColor(it)

        }
    }

    private fun setColor(color: Int) {

          _binding?.mainRe?.setBackgroundColor(color)
//          _binding?.undervalid?.setTextColor(color)
//          _binding?.pendingcashback?.setTextColor(color)
        _binding!!.cashback.setTextColor(color)

//             (_binding?.continueLi?.background as GradientDrawable).setColor(color)

        _binding!!.card1.setBackgroundColor(color)
             dialog?.window?.statusBarColor=color
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

    override fun showLoader() {

    }

    override fun dismissLoader() {

    }

    override fun showMess(mess: String?) {
        Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show()
    }
}
