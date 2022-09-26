package app.tapho.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import app.tapho.R
import app.tapho.databinding.FragmentNeedSupportBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.BaseFragment
import app.tapho.ui.help.model.SupportServiceListRes
import app.tapho.utils.toast

class NeedSupportFragment : BaseFragment<FragmentNeedSupportBinding>(), ApiListener<BaseRes, Any?> {
    private var type: String? = ""
    private var typeApi: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNeedSupportBinding.inflate(inflater, container, false)
        type = arguments?.getString("TYPE")

        initViews()
        return _binding?.root
    }

    private fun initViews() {
        when (type) {
            "support" -> {
                typeApi = "1"
                binding.titleTv.text = getString(R.string.message_us)
                binding.btnSent.text = getString(R.string.sent)
                binding.liRaiseTicket.visibility = View.GONE
                binding.reNumber.visibility = View.GONE
            }
            "call" -> {
                typeApi = "3"
                binding.titleTv.text = getString(R.string.call_request)
                binding.btnSent.text = getString(R.string.request_call)
                binding.subjectEt.visibility = View.GONE
                binding.liRaiseTicket.visibility = View.GONE
            }
            "ticket" -> {
                typeApi = "4"
                binding.titleTv.text = getString(R.string.raise_ticket)
                binding.btnSent.text = getString(R.string.submit_ticket)
                binding.subjectEt.visibility = View.GONE
                binding.reNumber.visibility = View.GONE
                getServiceList()
            }
            else -> {
                binding.scrollView.visibility = View.GONE
                binding.btnSent.visibility = View.GONE
                binding.addressIv.visibility = View.VISIBLE
                binding.addressTv.visibility = View.VISIBLE
            }
        }

        binding.editNumberTv.setOnClickListener {
            binding.numberEt.isEnabled = true
        }
        binding.btnSent.setOnClickListener { support() }
    }

    private fun support() {
        if(binding.messageEt.text.isEmpty()){
            showMess("Please enter description")
            return
        }

        var service = ""
        service = if (typeApi == "4")
            binding.spService.selectedItem.toString()
        else
            binding.numberEt.text.toString()

        viewModel.support(getSharedPreference().getUserId(),
            binding.subjectEt.text.toString(),
            binding.messageEt.text.toString(),
            service,
            typeApi,
            this,
            this)
    }

    private fun getServiceList() {
        viewModel.getSupportServiceList(getSharedPreference().getUserId(),
            this,
            object : ApiListener<SupportServiceListRes, Any?> {
                override fun onSuccess(t: SupportServiceListRes?, mess: String?) {
                    t?.let {
                        val list = ArrayList<String>()
                        t.data?.forEach {
                            it.name?.let { it1 -> list.add(it1) }
                        }
                        binding.spService.adapter = ArrayAdapter(requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            list)
                    }
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            NeedSupportFragment().apply {
                arguments = Bundle().apply {
                    putString("TYPE", type)
                }
            }
    }

    override fun onSuccess(t: BaseRes?, mess: String?) {
        context?.toast(mess)
        activity?.onBackPressed()
    }
}