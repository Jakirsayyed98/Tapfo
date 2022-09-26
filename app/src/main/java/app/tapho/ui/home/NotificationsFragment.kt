package app.tapho.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentNotificationsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.help.SupportServiceActivity
import app.tapho.ui.home.adapter.NotificationAdapter
import app.tapho.ui.model.NotificationRes

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>(),
    ApiListener<NotificationRes, Any?> {
    private var mAdapter: NotificationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        init()
        return _binding?.root
    }

    private fun init() {
        binding.helpIv.setOnClickListener {
            startActivity(Intent(context, SupportServiceActivity::class.java))
        }
        mAdapter = NotificationAdapter()
        binding.liEmpty.visibility = View.GONE
        binding.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.recyclerNotification.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        getData()
    }

    private fun getData() {
        viewModel.getNotificationList(getSharedPreference().getUserId(), this, this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NotificationsFragment()
    }

    override fun onSuccess(t: NotificationRes?, mess: String?) {
        t?.let {
            mAdapter?.addAllItem(it.data)
        }
    }
}