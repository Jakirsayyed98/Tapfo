package app.tapho.ui.localbizzUI.BusinessProfileFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelectedBusinessReviewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.ProfilesRatingAndReviewsShowAdapter
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Rating
import app.tapho.ui.localbizzUI.Model.getBusinessDetails.getBusinessDetailRes


class SelectedBusinessReviewFragment : BaseFragment<FragmentSelectedBusinessReviewBinding>() {

    var RatingAndReviewsAdapter : ProfilesRatingAndReviewsShowAdapter<Rating>? = null
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
        _binding = FragmentSelectedBusinessReviewBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        getAllReviewsList()
        setRatingAndReviewsLayout()

        return _binding?.root
    }

    private fun setRatingAndReviewsLayout() {
        RatingAndReviewsAdapter = ProfilesRatingAndReviewsShowAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })
        _binding!!.ratingAndReviews.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = RatingAndReviewsAdapter
        }
    }

    private fun getAllReviewsList() {
        viewModel.getbusinessDetails(getSharedPreference().getUserId(),activity?.intent?.getStringExtra("business_id"),this,object : ApiListener<getBusinessDetailRes,Any?>{
            override fun onSuccess(t: getBusinessDetailRes?, mess: String?) {
                t!!.data.let {
                    _binding!!.tvTitle.text = it.get(0).business_name
                    _binding!!.ratings.text ="Ratings and reviews: "+ it.get(0).average_rating+"/5"

                   it.get(0).rating_list.let {
                       if (it.isNullOrEmpty().not()) {
                           RatingAndReviewsAdapter!!.addAllItem(it)
                           _binding!!.noreview.visibility = View.GONE
                       }else{
                           _binding!!.noreview.visibility = View.VISIBLE
                       }
                   }
                }
            }

        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectedBusinessReviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}