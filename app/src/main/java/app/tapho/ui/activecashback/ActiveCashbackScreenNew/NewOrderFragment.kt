package app.tapho.ui.activecashback.ActiveCashbackScreenNew

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.FragmentCashbackOrderBinding
import app.tapho.databinding.FragmentNewOrderBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.DATA
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson


class NewOrderFragment : BaseFragment<FragmentNewOrderBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        NewOrderBinding = FragmentNewOrderBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        NewOrderBinding?.backIv?.setOnClickListener {
//            val intent = Intent(context, HomeActivity::class.java)
//            startActivity(intent)

            activity?.onBackPressed()
        }
        return NewOrderBinding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(t: TCashDashboardData?, showBack: Boolean) =
            NewOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA, Gson().toJson(t))
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Gson().fromJson(arguments?.getString(DATA), TCashDashboardData::class.java)
            ?.let { data ->
                setData(data)
                dateFunction(data)
            }
    }

  //  @SuppressLint("ResourceType")
    private fun setData(data: TCashDashboardData) {


        NewOrderBinding?.image?.let { it1 ->
            Glide.with(this).asBitmap().load(data.image)
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
        val cashbackAmt = data.user_commision.toString()
        val transactionAmt = data.sale_amount.toString()
        val percentage = cashbackAmt.toDouble() / transactionAmt.toDouble() * 100

        NewOrderBinding?.invite?.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.setPackage("com.whatsapp")
            val appLink:String="https://play.google.com/store/apps/details?id=app.tapho"
            intent.putExtra(Intent.EXTRA_TEXT, "Hey\n"+appLink)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
        NewOrderBinding?.nameTv?.text = data.offer_name
        // _binding?.nameTv2?.text = getString(R.string.activate, data.offer_name)
        NewOrderBinding?.orderAmountTv?.text = withSuffixAmount(data.sale_amount)
        NewOrderBinding?.transactionDate?.text = " Transaction Completed. " + data.date
        val finalValue = String.format("%.2f", percentage).toDouble()
        NewOrderBinding?.cashbackAmountTv?.text = finalValue.toString() + "% Cashback"//  data.user_commision+"% Cashback"
//        _binding?.refrenceIdTv?.text =
//            SpannableString(getString(R.string.reference_id_, data.affiliate_id)).apply {
//                setSpan(StyleSpan(Typeface.BOLD), 13, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//            }
        NewOrderBinding?.redirectline?.text = getString(R.string.cashback_recieved_from, data.offer_name)
        NewOrderBinding?.redirectline1?.text = getString(R.string.cashbackhold, data.offer_name)

        if (data.status?.uppercase() == "VERIFIED") {
            NewOrderBinding?.verifedConformation?.setImageResource(R.drawable.verifiedok)
            NewOrderBinding?.backgorundtext?.text = getString(
                R.string.your_cashback_verified,
                withSuffixAmount(data.user_commision),
                data.offer_name
            )
            NewOrderBinding?.cashbackStatus?.text = getString(R.string.verified_from, data.offer_name)
        } else if (data.status?.uppercase() == "VALIDATED") {
            NewOrderBinding?.verifedConformation?.setImageResource(R.drawable.verifiedok)
            NewOrderBinding?.backgorundtext?.text = getString(
                R.string.your_cashback_verified,
                withSuffixAmount(data.user_commision),
                data.offer_name
            )
            NewOrderBinding?.cashbackStatus?.text = getString(R.string.verified_from, data.offer_name)
        } else if (data.status?.uppercase() == "PENDING") {
            NewOrderBinding?.backgorundtext?.text = getString(
                R.string.your_cashback_pending,
                withSuffixAmount(data.user_commision),
                data.offer_name
            )
            NewOrderBinding?.cashbackStatus?.text = getString(R.string.pending_from, data.offer_name)
        } else {
            NewOrderBinding?.verifedConformation?.setImageResource(R.drawable.rejectedok)
            NewOrderBinding?.backgorundtext?.text = getString(
                R.string.your_cashback_rejected,
                withSuffixAmount(data.user_commision),
                data.offer_name
            )
            NewOrderBinding?.cashbackStatus?.text = getString(R.string.Rejected_from, data.offer_name)
        }


        val color = when (data.status?.uppercase()) {
            "VERIFIED" -> ContextCompat.getColor(
                requireContext(),
                R.color.green_dark
            )
            "VALIDATED" -> ContextCompat.getColor(
                requireContext(),
                R.color.green_dark
            )
            "PENDING" -> ContextCompat.getColor(requireContext(), R.color.offer_coupon)
            else -> ContextCompat.getColor(requireContext(), R.color.red)
        }
        NewOrderBinding?.statusTextTv?.text = getString(R.string.cashback_pending_from_,data.status,data.offer_name)
        NewOrderBinding?.statusTextTv?.setTextColor(color)
        NewOrderBinding?.backgorundtext?.setBackgroundColor(color)


        NewOrderBinding?.tapfoTransactionID?.text = data.trans_id + "-" + data.user_id
        NewOrderBinding?.statusTextTv?.setTextColor(color)
        NewOrderBinding?.merchantId?.text = data.trans_id
        NewOrderBinding?.arrowdown?.setOnClickListener {
            if (NewOrderBinding?.VisibilityLayout?.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(NewOrderBinding?.cardview)
                NewOrderBinding?.VisibilityLayout?.visibility = View.GONE
                NewOrderBinding?.arrowdown?.setImageResource(R.drawable.arrow_down)
            } else {
                TransitionManager.beginDelayedTransition(NewOrderBinding?.cardview)
                NewOrderBinding?.VisibilityLayout?.visibility = View.VISIBLE
                NewOrderBinding?.arrowdown?.setImageResource(R.drawable.arrow_up)
            }
        }

    }

    private fun dateFunction(data: TCashDashboardData) {
        if (data.date.toString().length == 10) {
            Log.d("Date", data.date.toString())
            val date100 = data.date.toString().trim() + " 10/10/10"
            val date0 = date100
            val data1 = date0//.trim()
            val length1 = data1.length
            val data2 = data1.removeRange(10, 19)
            val data3 = data2;

            val data4 = data3.reversed()
            //start
            val data5 = data4.dropLast(8) //81
            val maindate = data5.reversed()//18
            //end
            //start 1
            val data6 = data1.removeRange(7, 18)
            val data8 = data6.removeRange(0, 4)

            val data7 = data8.reversed()
            val mainMonth = data7.removeRange(2, 4)
            //end 1
            //statrt2
            val year = data3.removeRange(4, 10)

            var month: String? = null
            if (mainMonth.equals("01")) {
                month = "Jan"
            } else if (mainMonth.equals("02")) {
                month = "Feb"
            } else if (mainMonth.equals("03")) {
                month = "Mar"
            } else if (mainMonth.equals("04")) {
                month = "Apr"
            } else if (mainMonth.equals("05")) {
                month = "May"
            } else if (mainMonth.equals("06")) {
                month = "Jun"
            } else if (mainMonth.equals("07")) {
                month = "Jul"
            } else if (mainMonth.equals("08")) {
                month = "Aug"
            } else if (mainMonth.equals("09")) {
                month = "Sep"
            } else if (mainMonth.equals("10")) {
                month = "Oct"
            } else if (mainMonth.equals("11")) {
                month = "Nov"
            } else if (mainMonth.equals("12")) {
                month = "Dec"
            }

            val dataMonth = month.toString()
            val actualDate = dataMonth + " " + maindate + ", " + year

            NewOrderBinding?.transactionDate?.text = " Transaction Completed. " +  actualDate
            NewOrderBinding?.transactionDate1?.text=actualDate

        }
        else if (data.date.toString().length == 19) {
            val date100 = data.date.toString().trim()
            val date0 = date100
            val data1 = date0//.trim()
            val length1 = data1.length
            val data2 = data1.removeRange(10, 19)
            val data3 = data2;

            val data4 = data3.reversed()
            //start
            val data5 = data4.dropLast(8) //81
            val maindate = data5.reversed()//18
            //end
            //start 1

            val data6 = data1.removeRange(7, 19)
            val data8 = data6.removeRange(0, 5)
            val data7 = data8//.reversed()

            val mainMonth = data7
            //end 1
            //statrt2
            val year = data3.removeRange(4, 10)

            var month: String? = null
            if (mainMonth.equals("01")) {
                month = "Jan"
            } else if (mainMonth.equals("02")) {
                month = "Feb"
            } else if (mainMonth.equals("03")) {
                month = "Mar"
            } else if (mainMonth.equals("04")) {
                month = "Apr"
            } else if (mainMonth.equals("05")) {
                month = "May"
            } else if (mainMonth.equals("06")) {
                month = "Jun"
            } else if (mainMonth.equals("07")) {
                month = "Jul"
            } else if (mainMonth.equals("08")) {
                month = "Aug"
            } else if (mainMonth.equals("09")) {
                month = "Sep"
            } else if (mainMonth.equals("10")) {
                month = "Oct"
            } else if (mainMonth.equals("11")) {
                month = "Nov"
            } else if (mainMonth.equals("12")) {
                month = "Dec"
            }

            val dataMonth1 = month.toString()
            val actualDate1 = dataMonth1 + " " + maindate + ", " + year

            NewOrderBinding?.transactionDate?.text = " Transaction Completed. " + actualDate1
            NewOrderBinding?.transactionDate1?.text=actualDate1


        }
        else {
            NewOrderBinding?.transactionDate?.text = " Transaction Completed. " + data.date
            NewOrderBinding?.transactionDate1?.text=data.date

        }
    }


    private fun setColor(bitmap: Bitmap) {
//        val defColor = ContextCompat.getColor(requireContext(), R.color.black)
        createPaletteSync(bitmap).vibrantSwatch?.rgb?.let { setColor(it) }
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    private fun setColor(color: Int) {
        // (_binding?.continueLi?.background as GradientDrawable).setColor(color)
    }
}