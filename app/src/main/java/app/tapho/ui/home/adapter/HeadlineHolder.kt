package app.tapho.ui.home.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ContainerActivity
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewHomeRes.Headline
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.model.NewHomeRes.MiniAppX
import app.tapho.utils.OPEN_WEB_VIEW
import com.google.common.collect.Lists

class HeadlineHolder(
    val binding: RowHeadlineBinding,
    private val clickListener: RecyclerClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(headline: /*AppCategory*/Headline) {

        binding.nameTv.text =headline.heading
       // binding.headlineTv.text =

        binding.Categoryname.text="Explore more "+headline.name+" categories"

        binding.AllCategories.setOnClickListener {
            ContainerActivity.openContainer(itemView.context, "MiniAppFragment", headline, false, headline.name)

        }

     //   val mAdapter = UniversalAdapter<MiniApp>(object : RecyclerClickListener {
        val mAdapter = MiniAppHeadlinesAdapter<MiniAppX>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp) {
                    clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
                } else {
                    clickListener.onRecyclerItemClick(0, headline, "MiniAppFragment")
                }
            }
        })

      //  mAdapter.setMoreImagePos(4)
        binding.recyclerHeadline.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
        headline.mini_app?.let {
            val partitionedList: List<List<MiniAppX>> = Lists.partition(it, 6)
            mAdapter.addAllItem(partitionedList[0])
        }
    }

}