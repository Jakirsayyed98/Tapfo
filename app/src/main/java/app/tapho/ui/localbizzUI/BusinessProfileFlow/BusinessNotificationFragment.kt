package app.tapho.ui.localbizzUI.BusinessProfileFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentBusinessNotificationBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.UserFlow.CustomeData.CustomeModel
import app.tapho.ui.localbizzUI.UserFlow.CustomeData.businessCustomeAdapter


class BusinessNotificationFragment : BaseFragment<FragmentBusinessNotificationBinding>() {


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
        _binding = FragmentBusinessNotificationBinding.inflate(inflater,container,false)

        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.toolbar.tvTitle.text = "Notification"

        setCutomeReyclerViewData()

        return _binding?.root
    }

    private fun setCutomeReyclerViewData() {
        var businesess = businessCustomeAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is CustomeModel)
                    LocalbizContainerActivity.openContainer(requireContext(),"ShowSelectedBusinessData",data,false,data.name)
            }

        }).apply {
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","Bholu motton shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","kirana shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","data store",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","Bholu motton shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","kirana shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","data store",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","Bholu motton shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","kirana shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","data store",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","Bholu motton shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","kirana shop",""))
            addItem(CustomeModel("https://media.fashionnetwork.com/m/8dd2/c955/8635/c3bb/f5d2/2bad/ed8f/9563/2eed/e5e7/e5e7.jpg","data store",""))
        }

        _binding!!.AllListedbusinesspartnar.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = businesess
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BusinessNotificationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}