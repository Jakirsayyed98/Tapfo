package app.tapho.ui.profile

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentHowOnlineCashbackWorksBinding
import app.tapho.ui.BaseFragment
import com.bumptech.glide.Glide


class HowOnlineCashbackWorksFragment : BaseFragment<FragmentHowOnlineCashbackWorksBinding>() {

    val image = "https://tapfo.in/delta/public/storage/all_images/202212021623481669978428s9wx3HbJv8.png"


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
        _binding = FragmentHowOnlineCashbackWorksBinding.inflate(inflater,container,false)

        statusBarColor(R.color.white)
        statusBarTextWhite()
//        Glide.with(requireContext()).load(R.drawable.how_online_cashback_work).centerCrop().into(_binding!!.icon)
        Glide.with(requireContext()).load(image).centerCrop().into(_binding!!.icon)
        _binding!!.backIv.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }




        return _binding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HowOnlineCashbackWorksFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}