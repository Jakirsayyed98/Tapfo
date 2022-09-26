package app.tapho.ui.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMobileRechargeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.model.AppCategory
import app.tapho.ui.recharge.adapter.SearchContactsAdapter
import app.tapho.ui.recharge.adapter.StaredContactsAdapter
import app.tapho.ui.recharge.model.ContactFetchModel
import app.tapho.ui.recharge.model.ContactsModel
import app.tapho.utils.DATA


class MobileRechargeBaseFragment : BaseFragment<FragmentMobileRechargeBinding>(),
    RecyclerClickListener {
    private var data: AppCategory? = null
    private var searchAdapter: SearchContactsAdapter? = null
    private var starAdapter: StaredContactsAdapter? = null
    private val list = ArrayList<ContactsModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMobileRechargeBinding.inflate(inflater, container, false)

        init()
        return binding.root
    }

    private fun init() {
        data = arguments?.getSerializable(DATA) as AppCategory?
        data?.let {
            (activity as RechargeActivity).setRTitle(it.name)
        }
        binding.search.doAfterTextChanged {
            search(it.toString())
        }
        setStaredContactsRecycler()
        if (list.isEmpty())
            ViewModelProvider(this).get(ContactFetchModel::class.java)
                .getAllContacts(context, object : ApiListener<ArrayList<ContactsModel>, Any?> {
                    override fun onSuccess(t: ArrayList<ContactsModel>?, mess: String?) {
                        t?.let { list.addAll(it) }
                        setStartContacts()
                    }
                })

        setSearchRecycler()
        binding.proceedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MobileRecharge_to_rechargeAmountFragment,
                Bundle().apply { putSerializable(DATA, this@MobileRechargeBaseFragment.data) })
        }
    }

    private fun fetchContacts(){

    }
    companion object {
        @JvmStatic
        fun newInstance() =
            MobileRechargeBaseFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    private fun setSearchRecycler() {
        if (searchAdapter == null)
            searchAdapter = SearchContactsAdapter(this)
        binding.recyclerSearched.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun setStaredContactsRecycler() {
        if (starAdapter == null)
            starAdapter = StaredContactsAdapter()
        binding.recyclerStaredContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = starAdapter
        }
    }

    private fun search(s: String) {
        searchAdapter?.clear()
        if (s.isNotEmpty())
            list.forEach {
                if (it.name.contains(s, true) || it.phone.contains(s, true)) {
                    searchAdapter?.addItem(it)
                }
            }
    }

    private fun setStartContacts() {
        starAdapter?.clear()
        list.forEach {
            if (it.isStared)
                starAdapter?.addItem(it)
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "operator")
            findNavController().navigate(R.id.action_FirstFragment_to_SelectOperator,
                Bundle().apply { putSerializable(DATA, this@MobileRechargeBaseFragment.data) })
    }


}