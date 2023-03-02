package app.tapho.ui.favourite

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.network.Status
import app.instagst.ui.interfaces.LoaderListener2
import app.tapho.NavSheet.Fragment_favorite_nav
import app.tapho.R
import app.tapho.databinding.DialogFavouriteFragmentBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.LoaderFragment
import app.tapho.ui.home.HomeTabFragment
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.MiniApp
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.toast
import app.tapho.viewmodels.CategoriesDataViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavouriteDialogFragment : DialogFragment(), RecyclerClickListener, LoaderListener2 {
    private var binding: DialogFavouriteFragmentBinding? = null
    private var viewModel: CategoriesDataViewModel? = null
    private var mAdapter: FavouriteAdapter? = null
    private var isAnyChange: Boolean = false

    companion object {
        fun newInstance(tabs: ArrayList<AppCategory>?): FavouriteDialogFragment {
            val args = Bundle()
            args.putString("TABS", Gson().toJson(tabs))

            val fragment = FavouriteDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogFavouriteFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CategoriesDataViewModel::class.java)
        init()
//        dialog?.window?.addFlags(  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            //    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        dialog?.window?.statusBarColor=Color.parseColor("#FFFFFF")
        binding?.toolbar?.tvTitle?.text="Add to Favourites"
        binding?.toolbar?.tvTitle?.visibility=View.VISIBLE


        return binding?.root
    }

    private fun init() {
        binding?.toolbar?.backIv?.setOnClickListener {
         dismiss()
        }
        setTabs()
    }

    private fun setTabs() {
        val mAdapter = CategoryTabAdapter<AppCategory>(this)
//      mAdapter.addItem(AppCategory("", "", "All merchants", "", true))
        binding?.recyclerTabs?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
        val listType = object : TypeToken<ArrayList<AppCategory>>() {}.type
        val list = Gson().fromJson<ArrayList<AppCategory>>(arguments?.getString("TABS"), listType)
        if (list.isNullOrEmpty().not()){
            mAdapter.addAllItem(list)
        }

        setDataRecycler()
        observerData()
        if (list.size > 0) {
            list[0].isSelected = true
            getData(list[0])
        }
    }

    private fun setDataRecycler() {
        mAdapter = FavouriteAdapter(this)
        binding?.recyclerData?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is AppCategory) {
            getData(data)
        } else if (data is MiniApp && type == "favourite") {
            viewModel?.makeFev(pos,
                if (data.is_favourite == "1") "unFev" else "Fev",
                AppSharedPreference.getInstance(requireContext())
                    .getUserId(),
                data.id,
                this)
        }
    }

    private fun getData(data: AppCategory) {
        mAdapter?.clear()
        viewModel?.getCategoryList(AppSharedPreference.getInstance(requireContext())
            .getUserId(), data.id)
    }

    private fun observerData() {
        viewModel?.getData()?.observe(viewLifecycleOwner,
            {
                if (it.status == Status.SUCCESS) {
                    binding?.shimmerViewContainer?.stopShimmer()
                    binding?.shimmerViewContainer?.visibility = View.GONE
                    it.data?.let { it1 -> mAdapter?.addAllItem(it1) }
                } else if (it.status == Status.LOADING) {
                    binding?.shimmerViewContainer?.visibility = View.VISIBLE
                    binding?.shimmerViewContainer?.startShimmer()
                } else if (it.status == Status.ERROR) {
                    binding?.shimmerViewContainer?.stopShimmer()
                    binding?.shimmerViewContainer?.visibility = View.GONE
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun showLoader() {
        LoaderFragment.showLoader(childFragmentManager)
    }

    override fun dismissLoader() {
        LoaderFragment.dismissLoader(childFragmentManager)
    }

    override fun showMess(mess: String?) {
        context?.toast(mess)
    }

    override fun onSuccess(pos: Int, data: Any?, mess: String?, type: String?) {
        isAnyChange = true
        if (type != null) {
            mAdapter?.makeFev(pos, type)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
//        if (parentFragment is HomeFragment && isAnyChange) {
//            (parentFragment as HomeFragment).refreshFavData()
//        }
        if (parentFragment is Fragment_favorite_nav && isAnyChange) {
            (parentFragment as Fragment_favorite_nav).refreshFavData()
        }

        if (parentFragment is HomeTabFragment && isAnyChange) {
            (parentFragment as HomeTabFragment).refreshFavData()
        }

    }

}