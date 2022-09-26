package app.tapho.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.FragmentGameBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.merchants.MerchantOfferFragment
import app.tapho.ui.model.Popular
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.getCustomTab
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.random.Random


class GameFragment : BaseFragment<FragmentGameBinding>(), ApiListener<Games, Any?>,
    RecyclerClickListener {
    private var madapter: PagerFragmentAdapter? = null
    var dataget: String? = null
    private var games: ArrayList<Data>? = null
    var intColor:Int?=null
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
        bindingForGames = FragmentGameBinding.inflate(inflater, container, false)

        viewmodeldata()
        setCashbackMerchantsPager()

        return bindingForGames?.root
    }


    private fun viewmodeldata() {
        viewModel.getAllGames(
            AppSharedPreference.getInstance(requireContext()).getUserId(), this, this )
    }

    private fun setCashbackMerchantsPager() {
        madapter = PagerFragmentAdapter(this)
        bindingForGames!!.pagerCashbackMerchantsFragment.adapter = madapter
        madapter!!.addFragment(GamesForYouFragment.newInstance(), getString(R.string.Games_ForYou))
        madapter!!.addFragment(GamesFavouriteFragment.newInstance(), getString(R.string.Games_Favourite))
        //bindingForGames!!.tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_on_primary))
        bindingForGames!!.tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.white), ContextCompat.getColor(requireContext(), R.color.white))
        TabLayoutMediator(
            bindingForGames!!.tabLayout,
            bindingForGames!!.pagerCashbackMerchantsFragment
        ) { tab, position ->
            tab.customView = getCustomTab(context, madapter?.getTitle(position))
        }.attach()
    }

    companion object {
        var listdata:String?=null
        @JvmStatic
        fun newInstance() =
            GameFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: Games?, mess: String?) {
            listdata=t.toString()
        t?.let {

            it.data.let {
                setUiData(it)
            }
        }
    }

    private fun setUiData(it: List<Data>) {
        var listSize = it.size

        val random1 = (0..listSize).shuffled().last()
        Glide.with(requireContext()).load(it.get(random1).assets.wall)
            .listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Palette.from(resource!!.toBitmap()).generate {
                            palette->palette.let {
                        intColor=it?.vibrantSwatch?.rgb?:0
                        statusBarColorChange(intColor!!)
                    }
                    }
                    return false
                }

            }).centerCrop()
            .into(bindingForGames!!.bannerImage)

        bindingForGames!!.bannerImage.setOnClickListener { click ->
            GameWebViewActivity.openWebView(context,it[random1].url,it[random1].id,it[random1].name,it[random1].assets.square,"")
         //   WebViewActivity.openWebView(context, it[random1].url, "", "", "", "", "")
        }
        bindingForGames!!.spain.setOnClickListener {
            //  GameWebViewActivity.openWebView(context,"https://quizzop.com/?id=3375","","","","")
         WebViewActivity.openWebView(context, "https://quizzop.com/?id=3375", "", "", "", "", "","")
        }
        bindingForGames!!.searchTool12.setOnClickListener {
            searchClick()
        }
        bindingForGames!!.shuffel.setOnClickListener {click->
            val random = (0..listSize).shuffled().last()

            GameWebViewActivity.openWebView(context,it[random].url,it[random].id,it[random].name,it[random].assets.square,"")
         //   WebViewActivity.openWebView(context, it[random].url, "", "", "", "", "")
        }
        bindingForGames!!.discription.text = it.get(random1).description
        bindingForGames!!.Gamename.text = it.get(random1).name
        playsCount(it.get(random1).gamePlays.toInt())

    }

    private fun searchClick() {
        openContainer("GameSearch", "", false, "data.name")
    }

    private fun statusBarColorChange(intColor: Int) {
       requireActivity().window.statusBarColor=intColor
    }
    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }
    private fun playsCount(gameplays: Int) {

        if (gameplays > 999 && gameplays < 1000000) {
            val data = gameplays / 1000
            data.rangeTo(1)
            bindingForGames!!.plays.text = data.toString() + "k"
        } else if (gameplays > 1000000 && gameplays < 1000000000) {
            val data = gameplays / 1000
            data.rangeTo(1)
            bindingForGames!!.plays.text = data.toString() + "M"
        } else if (gameplays > 1000000000 && gameplays < 1000000000000) {
            val data = gameplays / 1000
            data.rangeTo(1)
            bindingForGames!!.plays.text = data.toString() + "B"
        } else if (gameplays > 1000000000000 && gameplays < 1000000000000000) {
            val data = gameplays / 1000
            data.rangeTo(1)
            bindingForGames!!.plays.text = data.toString() + "T"
        } else if (gameplays > 1000000000000000) {
            val data = gameplays / 1000
            data.rangeTo(1)
            bindingForGames!!.plays.text = data.toString() + "Q"
        }

}


    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}

