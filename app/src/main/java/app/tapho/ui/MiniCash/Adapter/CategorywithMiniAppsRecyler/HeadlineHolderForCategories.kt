package app.tapho.ui.MiniCash.Adapter.CategorywithMiniAppsRecyler

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.ContainerActivity
import app.tapho.ui.model.*
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.viewmodels.MerchantsAllListViewModel

class HeadlineHolderForCategories(
    val binding: RowHeadlineBinding,
    private val clickListener: RecyclerClickListener,
) : RecyclerView.ViewHolder(binding.root) {
        var Miniapplist : ArrayList<NewCashback> = ArrayList()
            var categoriesid = ""
    var merchantViewMode: MerchantsAllListViewModel = MerchantsAllListViewModel()
    fun setData(categories: CashbackMerchantCategory) {
        val name = categories.name

//        binding.nameTv.text = name
        binding.Categoryname.text="Explore more "+name+" categories"
        categoriesid = categories.id.toString()

        binding.root.setOnClickListener {
            ContainerActivity.openContainer(itemView.context, "MiniAppFragment", categories, false, name)
        }

     //   val mAdapter = UniversalAdapter<MiniApp>(object : RecyclerClickListener {
        val mAdapter = CategoriesMiniAppHeadlinesAdapter<NewCashback>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp) {
                    clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
                } else {
                    clickListener.onRecyclerItemClick(0, categories, "MiniAppFragment")
                }
            }
        })

      //  mAdapter.setMoreImagePos(4)
        binding.recyclerHeadline.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }



        merchantViewMode.getCashbackMerchants(AppSharedPreference.getInstance(itemView.context).getUserId(), "0")
        merchantViewMode.getValue().observe(binding.root.context as LifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
//                    setData(it.data)

                    it.data?.let {
                        it.data!!.forEach {
                            if (it.id.toString() == categoriesid){
                                Miniapplist.add(it)
                            }
                        }
                    }

                }
            }
        })

        Miniapplist.let {
//            val partitionedList: List<List<CashbackMerchant>> = Lists.partition(it, 4)
//            mAdapter.addAllItem(partitionedList[0])
            mAdapter.addAllItem(if (it.size>4) it.subList(0,4) else it)
        }
    }



}