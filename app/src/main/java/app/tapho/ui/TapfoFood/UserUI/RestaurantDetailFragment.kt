package app.tapho.ui.TapfoFood.UserUI

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentRestaurantDetailBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.TapfoFood.UserUI.Adapter.AllDishesCategoryAdapter
import app.tapho.ui.TapfoFood.UserUI.Adapter.RestorantsOfferAndCouponsAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.model.HomeRes
import app.tapho.utils.*
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson


class RestaurantDetailFragment : BaseFragment<FragmentRestaurantDetailBinding>() {


    private var AllDishesAdapter : AllDishesCategoryAdapter? = null
    val categoryList: ArrayList<String> = ArrayList()
    private var linearLayoutManager: LinearLayoutManager? = null


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
        _binding = FragmentRestaurantDetailBinding.inflate(inflater,container,false)
//        statusBarColor(R.color.white)
        statusBarTextWhite()

        activity?.getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        _binding!!.veg.setOnClickListener {
            _binding!!.veg.isSelected =true
            _binding!!.nonVeg.isSelected =false
        }

        _binding!!.nonVeg.setOnClickListener {
            _binding!!.nonVeg.isSelected =true
            _binding!!.veg.isSelected =false
        }

        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        setAllPlansInData()
        getMerchantData()


       activity?.intent?.getStringExtra(DATA).let {
           val data = Gson().fromJson(it,FoodCustomeSuperCategoryModel::class.java)
           _binding!!.name.text =data.name
           _binding!!.name1.text =data.name
           Glide.with(requireContext()).asBitmap().load(data.image).into(object : BitmapImageViewTarget(_binding!!.icon) {
                   override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                       super.onResourceReady(resource, transition)
                       createPaletteSync(resource).vibrantSwatch?.rgb?.let {
                        //   _binding!!.banner.setCardBackgroundColor(it)
                          // _binding!!.iconBG.setCardBackgroundColor(it)
//                           statusBarColor(it)
                       }
                   }
               })
           _binding!!.viewCart.setOnClickListener {
               TapfoFoodContainerActivity.openContainer(requireContext(),"TapfoFoodCartFragment",data)
           }
           setAllRestorentsData()
       }
        return _binding?.root
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    private fun setAllRestorentsData() {
        val RestorantsOfferAndCouponsAdapter = RestorantsOfferAndCouponsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        }).apply {

            for (i in 1..10) {
                addItem(FoodCustomeSuperCategoryModel("", "10% off orders above", "1", ""))
            }

        }
        _binding!!.couponse.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = RestorantsOfferAndCouponsAdapter
        }
    }

    // only for see ui
    private fun getMerchantData() {
        viewModel.getHomeData("home",getUserId(),this,object :ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
             t!!.headlines.let {
                    it!!.forEach {
                        categoryList.add(it.name!!)
                    }

                 it.let {
                     AllDishesAdapter!!.addAllItem(it)
                 }
                 initTabLayout()
             }
            }
        })
    }


    private fun initTabLayout() {

        categoryList.forEach {
            _binding!!.tabLayout.addTab(_binding!!.tabLayout.newTab().setText(it))
        }
        initMediator()
    }

    private fun initMediator() {
        TabbedListMediator(
            _binding!!.AllDishes,
            _binding!!.tabLayout,
            categoryList.indices.toList()
        )



    }


    private fun setAllPlansInData() {
        AllDishesAdapter = AllDishesCategoryAdapter(object :RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (pos>=1){
                    _binding!!.viewCart.visibility = View.VISIBLE
                }else{
                    _binding!!.viewCart.visibility = View.GONE
                }
            }
        })
        _binding!!.AllDishes.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = AllDishesAdapter

        }
    }




    companion object {

        @JvmStatic
        fun newInstance() =
            RestaurantDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}