package app.tapho.ui.News.JustTrailForNews

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentBlankBinding
import app.tapho.databinding.FragmentSearchAndCompaireBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.utils.AppSharedPreference

class BlankFragment : BaseFragment<FragmentBlankBinding>() {

    val numberList:MutableList<String> =ArrayList()
    var tempList2: ArrayList<Data> = ArrayList()
    var page=1
    var isloading=false
    var limit=20

    lateinit var adapter: RecyclerAdapter
    lateinit var layout: LinearLayoutManager

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
        blackFragmentbinding = FragmentBlankBinding.inflate(inflater, container, false)


        layout = LinearLayoutManager(requireContext())

        blackFragmentbinding!!.recycler.layoutManager=layout
        getPage()
        getData(1,page)
        blackFragmentbinding!!.recycler.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    val visibleItemCount = layout.childCount
                    val pastVisble = layout.findFirstCompletelyVisibleItemPosition()
                    val total =adapter.itemCount

                    if (!isloading){

                        if ((visibleItemCount + pastVisble) >= total){
                            page++
                            getData(1,page)
                            getPage()
                        }
                    }

                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        return blackFragmentbinding?.root
    }

    fun getPage(){

        isloading=true

        blackFragmentbinding!!.progress.visibility=View.VISIBLE
        val start= (page-1) * limit
        val end = (page) * limit

        for (i in start .. end){
            numberList.add("Item" + i.toString())
        }
        Handler().postDelayed({
            if (::adapter.isInitialized){
                adapter.notifyDataSetChanged()
            }else{
                adapter = RecyclerAdapter(this)
                blackFragmentbinding!!.recycler.adapter = adapter
            }
            isloading=false
            blackFragmentbinding!!.progress.visibility=View.GONE
        },1000)
    }

    internal fun getData(id: Int,i:Int) {

        viewModel.getNews(AppSharedPreference.getInstance(requireContext()).getUserId(), id.toString(), i.toString(), this, object :
            ApiListener<getAllNewsdata, Any?> {
            override fun onSuccess(t: getAllNewsdata?, mess: String?) {
                t.let {
                    it!!.data.let {
                        it.forEach {
                            tempList2.add(it)
                        }
                      tempList2.forEach {
                          Log.d("Data","${it.id}")
                      }
                    }
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BlankFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    class RecyclerAdapter (val activity: BlankFragment): RecyclerView.Adapter<RecyclerAdapter.NumberViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.headline_layout2,parent,false))

        }

        override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
            holder.sliderItem.text =activity.numberList[position]
        }

        override fun getItemCount(): Int {
            return activity.numberList.size
        }

        class NumberViewHolder(v : View) : RecyclerView.ViewHolder(v){
            val sliderItem=v.findViewById<TextView>(R.id.headlines)
        }

    }

}