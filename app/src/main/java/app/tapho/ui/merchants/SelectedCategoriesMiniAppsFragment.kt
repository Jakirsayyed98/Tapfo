package app.tapho.ui.merchants

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentSelectedCategoriesMiniAppsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.NewBannerDataAdapter1
import app.tapho.ui.home.adapter.SliderModelMain
import app.tapho.ui.merchants.adapter.MiniAppsDataAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.*
import app.tapho.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class SelectedCategoriesMiniAppsFragment : BaseFragment<FragmentSelectedCategoriesMiniAppsBinding>(),RecyclerClickListener {
    var miniapps : MiniAppsDataAdapter? = null
    var CategoryID=""
    private var popularList: ArrayList<Popular>? = null


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
        _binding = FragmentSelectedCategoriesMiniAppsBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()

        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE


        getMiniAppsData()
        getData()
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.searchbtn.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }

        return _binding?.root
    }

    override fun onResume() {
        super.onResume()
   //     getData()
    }


    private fun getData() {
        activity?.intent?.getStringExtra(DATA)?.let {
            val category = Gson().fromJson(it, AppCategory::class.java)
            CategoryID = category.id.toString()
            _binding!!.categoryName.text = category.name

            getMiniAppCategoryData(category.id,"")
        }
    }

    private fun getMiniAppsData() {
        miniapps = MiniAppsDataAdapter(this)
        _binding!!.AllMiniApps.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = miniapps
        }
    }


    private fun getMiniAppCategoryData(category_id: String?,sub_category_id:String) {
        val ListofCB : ArrayList<MiniApp> = ArrayList()
        val ListofNonCB : ArrayList<MiniApp> = ArrayList()
        viewModel.getAppCategoryMiniApp(AppSharedPreference.getInstance(requireContext()).getUserId(), category_id, sub_category_id,this,
            object : ApiListener<MiniAppRes, Any?> {
                override fun onSuccess(t: MiniAppRes?, mess: String?) {
                    t.let {
//                        _binding!!.count.text = it!!.mini_app!!.size.toString()+" Merchants"

                        it!!.mini_app!!.forEach {
                            if (it.tcash.equals("1")){
                                ListofCB.add(it)
                            }else{
                                ListofNonCB.add(it)
                            }
                        }

                        miniapps!!.clear()
                        miniapps!!.addAllItem(ListofCB)
                        miniapps!!.addAllItem(ListofNonCB)

                        _binding!!.mainLayout.visibility = View.VISIBLE
                        _binding!!.progress.visibility = View.GONE
                        val lsitcb : ArrayList<MiniApp> = ArrayList()
                        ListofCB.forEach {
                            if (it.merchant_payout!!.image.isNullOrEmpty().not())
                                lsitcb.add(it)
                        }
                        lsitcb.shuffled()
                        setBannerLayouta(lsitcb)

                    }
                }
            })
    }

    private fun setBannerLayouta(listofCB: ArrayList<MiniApp>) {


        if (listofCB.isNullOrEmpty()) {
            _binding!!.banner9.visibility = View.GONE
            _binding!!.tabLayout1.visibility = View.GONE
            _binding!!.bannerlay1.visibility = View.GONE
        } else {
            _binding!!.banner9.visibility = View.VISIBLE
            _binding!!.tabLayout1.visibility = View.VISIBLE
            _binding!!.bannerlay1.visibility = View.VISIBLE
        }
        val list : ArrayList<MiniApp> =ArrayList()
        if (listofCB.size>=9){
            list.addAll(listofCB.subList(0,8))
        }else{
        list.addAll(listofCB)
        }

        val imageList = java.util.ArrayList<SliderModelMain>()

        for (x in list) {
            val image = x.merchant_payout!!.image
            if (image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(image, x.url, x.id,x.type, "", ""))
            }

        }
        _binding!!.banner9.adapter = NewBannerDataAdapter1(imageList, _binding!!.banner9, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is SliderModelMain){
                    ActiveCashbackForWebActivity.openWebView(requireContext(),data.url,data.id,"",data.tcash,"","","","","")
                }

            }
        })
        _binding!!.banner9.clipToPadding = false
        _binding!!.banner9.clipChildren = false
        _binding!!.banner9.offscreenPageLimit = 3
        _binding!!.banner9.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.banner9.setPadding(10, 0, 10, 0)

        //Handler Start
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.banner9.currentItem

                        if (i == list.size - 1) {
                            i = -1//0
                            _binding!!.banner9.currentItem = i
                        }
                        i++
                        _binding!!.banner9.setCurrentItem(i, true)
                        postDelayed(this, 4000)
                    }
                }
            }
            postDelayed(runnable, 4000)
        }



        TabLayoutMediator(_binding!!.tabLayout1,_binding!!.banner9,false) { _,_ -> }.attach()
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            SelectedCategoriesMiniAppsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
    if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun openWebView(data: MiniApp) {
        ActiveCashbackForWebActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id,data.url_type,data.name
        )
    }

    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        popularList?.let {
            list.addAll(it)
        }
        if (type == "1")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        else
            openContainer(
                getString(R.string.popular_merchants),
                list,
                true,
                getString(R.string.popular_merchants)
            )
    }





}