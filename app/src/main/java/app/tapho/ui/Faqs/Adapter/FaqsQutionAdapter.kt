package app.tapho.ui.Faqs.Adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.FaqsheadlinesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.Faqs.Model.Data
import app.tapho.ui.Faqs.Model.FaqQuestion
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.google.common.collect.Lists

class FaqsQutionAdapter ( val binding: FaqsheadlinesLayoutBinding, private val clickListener: RecyclerClickListener, )
    : RecyclerView.ViewHolder(binding.root) {
    fun setData(headline: /*AppCategory*/Data) {

        binding.name.text =headline.name
    //    binding.Categoryname.text="Explore more "+headline.name+" categories"


        //   val mAdapter = UniversalAdapter<MiniApp>(object : RecyclerClickListener {
        val mAdapter = faqsQutionLayoutAdapter<FaqQuestion>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp) {
                    clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
                } else {
                    clickListener.onRecyclerItemClick(0, headline, "MiniAppFragment")
                }
            }
        })

        //  mAdapter.setMoreImagePos(4)
        binding.recyclerFaqQutions.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
        }

        headline.faq_question?.let {
            val partitionedList : List<List<FaqQuestion>> = Lists.partition(it, 6)
            mAdapter.addAllItem(partitionedList[0])
        }
    }

}