package app.tapho.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.ContactOnWhatsapp
import app.tapho.R
import app.tapho.databinding.FragmentSupportListBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.help.adapter.ServicelListAdapter
import app.tapho.ui.help.model.CustomeServicelist

class SupportListFragment :BaseFragment<FragmentSupportListBinding>() {


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
        _binding = FragmentSupportListBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        serviceList()
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return _binding?.root
    }

    private fun serviceList() {
        val servicelist = ServicelListAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is CustomeServicelist){

                    when (data.type) {
                        "WhatsApp"->{
                            requireContext().ContactOnWhatsapp("8369197342")
                        }
                        else->{
                            ContainerActivity.openContainerForSupport(requireContext(),"NeedSupportFragment",data)
                        }

                    }
                }
            }

        }).apply {
//            addItem(CustomeServicelist("1",R.drawable.creat_ticket, "Create Ticket", "Raise tickets related to refund, cashback missing, order transcation or any general issues." ,"Create Ticket","4"))
  //          addItem(CustomeServicelist("2",R.drawable.service_whatsapp_icon, "WhatsApp Support", "Chat with us on whatsapp ","WhatsApp",""))
            addItem(CustomeServicelist("3",R.drawable.share_feedback, "Share Feedback", "Please share your valuable feedback with us","Share Feedback","2"))
            addItem(CustomeServicelist("4",R.drawable.request_callback, "Request Call Back", "Our team will get back to you as soon as possible.","Request Call Back","3"))
        }


        _binding!!.supportlist.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            adapter = servicelist
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SupportListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}