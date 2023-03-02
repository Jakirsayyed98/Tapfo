package app.tapho.ui.home.OnlineStoresAdapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ContainerActivity
import app.tapho.ui.model.Headline
import app.tapho.ui.model.MiniApp
import app.tapho.utils.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide

class OnlineHeadlineHolder(val binding: RowHeadlineBinding, private val clickListener: RecyclerClickListener, )
    : RecyclerView.ViewHolder(binding.root) {

    fun setData(headline: Headline) {
        binding.Categoryname.text=headline.name
        binding.headlines.text=headline.heading
        Glide.with(itemView.context).load(headline.image).placeholder(R.drawable.loding_app_icon).into(binding.icon1)

        binding.arrow.setOnClickListener {
            ContainerActivity.openContainer(itemView.context, "MiniAppFragment", headline, false, headline.name)
        }

        val  mAdapter = OnlinePageMiniAppsHeadlineAdapter<MiniApp>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                ContainerActivity.openContainer(itemView.context, "MiniAppFragment", headline, false, headline.name)
//                if (data is MiniApp) {
//                    clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
//                } else {
//                    clickListener.onRecyclerItemClick(0, headline, "MiniAppFragment")
//                }
            }
        })

        binding.recyclerHeadline.apply {
            layoutManager = GridLayoutManager(context,4)
            adapter = mAdapter
        }
        mAdapter.setMoreImagePos(4)
        headline.mini_app?.let {
            mAdapter.addAllItem(if (it.size>4) it.subList(0,4) else it)

        }
    }

}