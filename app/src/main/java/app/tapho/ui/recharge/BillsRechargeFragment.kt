package app.tapho.ui.recharge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentBillsRechargeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.vouchers.model.VoucherCategoryRes
import app.tapho.utils.DATA


class BillsRechargeFragment : BaseFragment<FragmentBillsRechargeBinding>(),
    /*ApiListener<VoucherCategoryRes, Any?>, */RecyclerClickListener {
//    private var itemTypeAdapter: ItemTypeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBillsRechargeBinding.inflate(inflater, container, false)

        setTitle()
      //  setRecycler()
        return _binding?.root
    }

    private fun setTitle() {
        (activity as RechargeActivity).setRTitle(getString(R.string.biils_recharge))
    }

    /*
    private fun setRecycler() {
        if (itemTypeAdapter == null) {
            itemTypeAdapter = ItemTypeAdapter(this)
        }
        binding.recycler.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = itemTypeAdapter
        }
        if (itemTypeAdapter?.itemCount == 0)
            getServices()
    }

    private fun getServices() {
        viewModel.getRechargeService(getUserId(), this, this)
    }

    override fun onSuccess(t: VoucherCategoryRes?, mess: String?) {
        t?.let {
            it.data!!.forEach {
                Log.d("Recharge","${it.image}   ${it.name} \n")
            }
            t.data?.let { it1 -> itemTypeAdapter?.addAllItem(it1) }
        }
    }
*/
    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is AppCategory) {
            if (data.id == "1" || data.id == "2") {
                findNavController().navigate(R.id.action_billRecharge_to_MobileRecharge,
                    Bundle().apply {
                        putSerializable(DATA, data)
                    })
            }
        }
    }
}