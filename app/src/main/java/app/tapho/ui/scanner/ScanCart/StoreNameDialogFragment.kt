package app.tapho.ui.scanner.ScanCart

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.DialogFavouriteFragmentBinding
import app.tapho.databinding.FragmentStoreNameDialogBinding
import app.tapho.network.RequestViewModel
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.model.BusinessDetail.Data
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.viewmodels.CategoriesDataViewModel
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StoreNameDialogFragment : BaseFragment<FragmentStoreNameDialogBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStoreNameDialogBinding.inflate(inflater, container, false)

        statusBarColor(R.color.white)
        statusBarTextWhite()
        val data = activity?.intent?.getStringExtra(DATA)
        if (data.isNullOrEmpty().not()){
            Gson().fromJson(data, Data::class.java).let {
                _binding!!.storename.text = it.business_name
                _binding!!.storearea.text = it.area
            }
        }


        _binding!!.startShopping.setOnClickListener {
            GlobalScope.launch {
                getDatabase(requireContext()).appDao().DeleteAllProduct()
            }
            startActivity(Intent(requireContext(), BarcodeScannerForProductActivity::class.java))
            activity?.finish()
        }


        return _binding?.root
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            StoreNameDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}