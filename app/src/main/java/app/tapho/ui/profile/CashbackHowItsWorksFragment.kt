package app.tapho.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentCashbackHowItsWorksBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import com.bumptech.glide.Glide

class CashbackHowItsWorksFragment : BaseFragment<FragmentCashbackHowItsWorksBinding>() {

    val image = "https://tapfo.in/delta/public/storage/all_images/202212021622461669978366LFZzxiEtig.png"

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
        _binding = FragmentCashbackHowItsWorksBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.howwork.isSelected = true
//        Glide.with(requireContext()).load(R.drawable.how_tapfo_works).centerCrop().into(_binding!!.icon)
        Glide.with(requireContext()).load(image).centerCrop().into(_binding!!.icon)
        _binding!!.backIv.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.howwork.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "HowOnlineCashbackWorksFragment", "", false, "")
        }

        return _binding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CashbackHowItsWorksFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}