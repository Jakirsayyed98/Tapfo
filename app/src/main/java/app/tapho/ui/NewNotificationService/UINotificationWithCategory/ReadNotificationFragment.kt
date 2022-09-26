package app.tapho.ui.NewNotificationService.UINotificationWithCategory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentReadNotificationBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.Data
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.NotificationListRes
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.adapterNotification.ReadNotificationAdapter
import app.tapho.utils.DATA
import app.tapho.utils.TITLE


class ReadNotificationFragment : BaseFragment<FragmentReadNotificationBinding>() {


    var ReadNotificationAdapter : ReadNotificationAdapter<Data>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadNotificationBinding.inflate(inflater,container,false)

        var NotificationID = activity?.intent?.getStringExtra(DATA)
        _binding!!.title.text = activity?.intent?.getStringExtra(TITLE)
        Log.e("NotificationType","${NotificationID!!.drop(1).dropLast(1)}")

        getNotificationViewmodelData(NotificationID!!)
        setLayoutData()
        return _binding?.root
    }



    private fun getNotificationViewmodelData(notificationID: String) {
        var id =notificationID.drop(1).dropLast(1)
        viewModel.getNewNotificationList(getUserId(),id,this,object : ApiListener<NotificationListRes,Any?>{
            override fun onSuccess(t: NotificationListRes?, mess: String?) {
                t.let {
                    it!!.data.let {
                        ReadNotificationAdapter!!.addAllItem(it)
                    }
                }
            }
        })

    }

    private fun setLayoutData() {
        ReadNotificationAdapter = ReadNotificationAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })

        _binding!!.notificationData.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
                reverseLayout = false
            }
            adapter = ReadNotificationAdapter
        }


    }


    companion object {


        @JvmStatic
        fun newInstance() =
            ReadNotificationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}