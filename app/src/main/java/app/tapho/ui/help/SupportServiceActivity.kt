package app.tapho.ui.help

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import app.tapho.R
import app.tapho.databinding.ActivitySupportServiceBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.adapter.PagerFragmentAdapter2

class SupportServiceActivity : BaseActivity<ActivitySupportServiceBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        val mAdapter = PagerFragmentAdapter2(this)
        mAdapter.addFragment(NeedSupportFragment.newInstance("support"), "")
        mAdapter.addFragment(NeedSupportFragment.newInstance("call"), "")
        mAdapter.addFragment(NeedSupportFragment.newInstance("ticket"), "")
        mAdapter.addFragment(NeedSupportFragment.newInstance("other"), "")
        binding.pager.adapter = mAdapter
        binding.pager.isUserInputEnabled = false

        setTab()
        binding.liNeedSupport.performClick()
    }

    private fun setTab() {
        binding.liNeedSupport.setOnClickListener { setClick(it as LinearLayout) }
        binding.liRequestCall.setOnClickListener { setClick(it as LinearLayout) }
        binding.liRaiseTicket.setOnClickListener { setClick(it as LinearLayout) }
        binding.liMailUs.setOnClickListener { setClick(it as LinearLayout) }
    }

    private fun setClick(view: LinearLayout) {
        binding.lineSupport.visibility = View.INVISIBLE
        binding.lineRequestCall.visibility = View.INVISIBLE
        binding.lineRaiseTicket.visibility = View.INVISIBLE
        binding.lineMailUs.visibility = View.INVISIBLE

        view.getChildAt(2).visibility = View.VISIBLE
        when (view.id) {
            R.id.liNeedSupport -> binding.pager.currentItem = 0
            R.id.liRequestCall -> binding.pager.currentItem = 1
            R.id.liRaiseTicket -> binding.pager.currentItem = 2
            R.id.liMailUs -> binding.pager.currentItem = 3
        }
    }
}