package app.tapho.ui.activecashback.ActiveCashbackScreenNew

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.FragmentActiveCashbackBinding
import app.tapho.databinding.FragmentActiveCashbackDialogBinding
import app.tapho.databinding.FragmentGamesForYouBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.WebViewActivity
import app.tapho.ui.activecashback.ActiveCashbackDialogFragment
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.webViewActivity2
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson

class ActiveCashbackFragment : BaseFragment<FragmentActiveCashbackBinding>(), ApiListener<MiniAppRes1, Any?> {
    var textCashback: String? = null
    var tcash: WebTCashRes? = null
    var miniApp: MiniAppRes1? = null
    var miniappID:String?=null
    val window: FragmentActivity? = this.activity
    private var madapter: PagerFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ActiveCasbackBinding = FragmentActiveCashbackBinding.inflate(inflater, container, false)
        return ActiveCasbackBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setListener()
        ActiveCasbackBinding?.continueLi?.setOnClickListener {
            activate()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ActiveCashbackFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun getData() {
        viewModel.getMiniAppTCash(
            AppSharedPreference.getInstance(requireContext()).getUserId(),
            activity?.intent?.getStringExtra(MINI_APP_ID),
            this,
            object : ApiListener<WebTCashRes, Any?>{
                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                    setData(t!!)
                }

            })
    }




    private fun setData(it: WebTCashRes) {
        tcash=it
        var url="https://tapfo.in/delta/public/storage/all_images/"
        miniappID=it.mini_app!!.get(0).id
        textCashback = it.data.get(0).cashback.toString()
        ActiveCasbackBinding?.cashbackTv?.text =textCashback
        ActiveCasbackBinding?.cashback1?.text ="to activate "+textCashback
        ActiveCasbackBinding?.productNameTv?.text = it.mini_app?.get(0)?.name+"\nCashback"
        ActiveCasbackBinding?.nameTv2?.text = getString(R.string.activate, it.mini_app?.get(0)?.name)
        ActiveCashbackDialogFragment.merchantAppName = it.mini_app?.get(0)?.name

        ActiveCasbackBinding?.storewallet?.text="My "+it.mini_app?.get(0)?.name+" Cashback"
        ActiveCasbackBinding?.merchantDeal?.text=it.mini_app?.get(0)?.name+" deals"
        ActiveCasbackBinding?.voucherTag?.text="save 8% on all "+it.mini_app?.get(0)?.name+" vouchers"

        //      it.data[0].cashback?.let { it1 -> setReward(it1) }
        ActiveCasbackBinding?.productIv?.let { it1 ->
            Glide.with(this).asBitmap().load(it.mini_app?.get(0)?.image).centerCrop().circleCrop()
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
        //  sendData(it)
    }


    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    private fun setColor(bitmap: Bitmap) {
        val defColor = ContextCompat.getColor(requireContext(), R.color.black)
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
        webViewActivity2.openWebView(context,activity?.intent?.getStringExtra(WEB_VIEW_URL),activity?.intent?.getStringExtra(
            MINI_APP_ID),
            activity?.intent?.getStringExtra(ICON_URL), activity?.intent?.getStringExtra(
            TCASH_TYPE),
            activity?.intent?.getStringExtra(IS_FAVOURITE),textCashback.toString(),
            activity?.intent?.getStringExtra(APPCATEGORYID))
//        if (activity is webViewActivity2) {
//            (activity as webViewActivity2).initWebView()
//        }

    }

    override fun onSuccess(t: MiniAppRes1?, mess: String?) {

    }
}