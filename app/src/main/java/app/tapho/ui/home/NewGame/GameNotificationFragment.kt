package app.tapho.ui.home.NewGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGameNotificationBinding
import app.tapho.databinding.FragmentNotificationBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.NewNotificationService.AllNotificationUI.Adapter.AllNotificationAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.NewGame.Adapter.GameNotificationAdapter
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.AllNotification.Data


class GameNotificationFragment : BaseFragment<FragmentGameNotificationBinding>(),
    ApiListener<AllNotificationRes, Any?>, RecyclerClickListener {

    var ReadNotificationAdapter : GameNotificationAdapter<Data>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameNotificationBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        getNotificationListViewmodel()
        setLayoutData()

        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return _binding!!.root
    }

    private fun setLayoutData() {
        ReadNotificationAdapter= GameNotificationAdapter(this)
        _binding!!.notificationData.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = ReadNotificationAdapter
        }
    }

    private fun getNotificationListViewmodel() {
        viewModel.getAllNotification(getUserId(),this,this)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GameNotificationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: AllNotificationRes?, mess: String?) {

        var GameNotificatonList :  ArrayList<Data> = ArrayList()
        t.let {
            it!!.data.let {
                it.forEach {
                    if (it.noti_type.equals("2")){
                        GameNotificatonList.add(it)
                    }
                }

            }
            ReadNotificationAdapter!!.addAllItem(GameNotificatonList)
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        getMiniAppData(data.toString())
    }

    private fun getMiniAppData(miniAppId: String) {
        viewModel.getMiniAppTCash(getUserId(),miniAppId,this,object : ApiListener<app.tapho.ui.model.WebTCashRes,Any?>{
            override fun onSuccess(t: app.tapho.ui.model.WebTCashRes?, mess: String?) {
                t.let {
                    it!!.mini_app!!.get(0).let {
                        ActiveCashbackForWebActivity.openWebView(requireContext(),it.url,it.id,it.image,it.tcash,it.is_favourite,it.cashback,it.app_category_id,it.url_type,it.name)
                    }
                }
            }

        })
    }


}