package app.tapho.ui.tcash

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTCashRedeemBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes

class TCashRedeemActivity : BaseActivity<FragmentTCashRedeemBinding>(),
    ApiListener<TCashDasboardRes, Any?>, RecyclerClickListener {
    private var mAdapter: TCashbackAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTCashRedeemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setRecycler()
    }


    private fun init() {
        binding.withdrawIv.setOnClickListener { }
        binding.balanceTv.text=intent.getStringExtra("TOTAL_BALENCE")
        setSpannable()
    }

    private fun setRecycler() {
        mAdapter = TCashbackAdapter(this)
        binding.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            TCashRedeemActivity()
    }

    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
        t?.let {
            t.data?.let { it1 ->
                mAdapter?.addAllItem(it1)
                binding.emptyListTv.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }


    private fun setSpannable() {
        binding.emptyListTv.text =
            SpannableString(getString(R.string.you_don_t_have_any_orders)).apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        setResult(RESULT_OK)
                        onBackPressed()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                        ds.color = ContextCompat.getColor(this@TCashRedeemActivity, R.color.purple_500)
                    }
                }, 45, 49, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        binding.emptyListTv.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        /*if(type=="detail" && data is TCashDashboardData){
            startActivity(Intent(context,TCashbackDetailActivity::class.java).apply {
                flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(DATA,Gson().toJson(data))
            })
        }*/
    }
}