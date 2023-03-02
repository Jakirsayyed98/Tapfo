package app.tapho.ui.Faqs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentHelpAndSupportBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.Faqs.Adapter.FaqsBaseAdapter
import app.tapho.ui.Faqs.Model.Faqsmodel


class HelpAndSupportFragment : BaseFragment<FragmentHelpAndSupportBinding>(),RecyclerClickListener {

    private var FaqsBaseAdapter: FaqsBaseAdapter? = null
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
        _binding = FragmentHelpAndSupportBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        faqviewmodeldata()
        setTextAndClickEventType()
        setAdapterData()
        return _binding?.root
    }

    private fun setAdapterData() {
        FaqsBaseAdapter = FaqsBaseAdapter(this)
        binding.Faqs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FaqsBaseAdapter
        }
    }

    private fun faqviewmodeldata() {
        viewModel.getFaqsModel(getUserId(),this,object : ApiListener<Faqsmodel,Any?>{
            override fun onSuccess(t: Faqsmodel?, mess: String?) {
                t.let {
                    FaqsBaseAdapter!!.addAllItem(it!!.data)
                }
            }
        })
    }

    private fun setTextAndClickEventType() {
        binding.apply {
            backbutton.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
            steelneedhelp.setOnClickListener {
                ContainerActivity.openContainer(requireContext(),"OpenServiceList","")
//                startActivity ( Intent(context, SupportServiceActivity::class.java ) )
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HelpAndSupportFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}