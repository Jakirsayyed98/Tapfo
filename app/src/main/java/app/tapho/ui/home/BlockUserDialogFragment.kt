package app.tapho.ui.home

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
import app.tapho.databinding.BlockUserScreenBinding
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

class BlockUserDialogFragment : DialogFragment(){
    private var binding: BlockUserScreenBinding? = null



    companion object {
        fun newInstance(tabs: ArrayList<AppCategory>?): BlockUserDialogFragment {
            val args = Bundle()
            args.putString("TABS", Gson().toJson(tabs))

            val fragment = BlockUserDialogFragment()
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
        binding = BlockUserScreenBinding.inflate(inflater, container, false)
        dialog!!.setCancelable(false)
        dialog?.window?.statusBarColor = Color.BLACK
        return binding?.root
    }



}