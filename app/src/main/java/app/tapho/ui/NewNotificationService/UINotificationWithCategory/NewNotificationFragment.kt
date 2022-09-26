package app.tapho.ui.NewNotificationService.UINotificationWithCategory

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentNewNotificationBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory.Data
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory.getNotificationCategory
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.adapterNotification.NewCustomeAllNotification

class NewNotificationFragment : BaseFragment<FragmentNewNotificationBinding>() {

    var NewCustomeAllNotification : NewCustomeAllNotification? = null

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
        _binding = FragmentNewNotificationBinding.inflate(inflater,container,false)
        statusBarTextWhite()

        getNotificationCategoryViewmodel()
        setNotificationCategoryLayout()
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        return _binding?.root
    }


    private fun getNotificationCategoryViewmodel() {
            viewModel.getNotificationCategory(getUserId(),this,object : ApiListener<getNotificationCategory,Any?>{
                override fun onSuccess(t: getNotificationCategory?, mess: String?) {
                    t!!.let {
                        it.data.let {
                            NewCustomeAllNotification!!.addAllItem(it)
                        }
                    }
                }

            })
    }

    private fun setNotificationCategoryLayout() {
        NewCustomeAllNotification = NewCustomeAllNotification(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data){
                    ContainerActivity.openContainer(requireContext(),"ReadNotification",data.id,false,data.name)
                }


            }
        })

        _binding!!.notificationCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = NewCustomeAllNotification
        }

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            NewNotificationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}