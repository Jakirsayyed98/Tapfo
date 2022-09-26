package app.tapho.ui.News

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.ActivityDiscoverBinding
import app.tapho.databinding.ActivityHeadlinesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.News.Adapter.CategoriesAdapter
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.home.adapter.popularitemAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW

class DiscoverActivity : BaseActivity<ActivityDiscoverBinding>(),ApiListener<getCategories,Any?>,RecyclerClickListener{
    var categoryData: CategoriesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectViewModel()
        setRecyclerBrand()
    }

    private fun connectViewModel() {
        viewModel.getNewsCategories(AppSharedPreference.getInstance(this).getUserId(),  this, this)
    }

    override fun onSuccess(t: getCategories?, mess: String?) {
        t!!.data.let {
            categoryData!!.addAllItem(it)
        }

    }
    private fun setRecyclerBrand() {
        categoryData = CategoriesAdapter(this)
        binding.Categories.apply {
            layoutManager = GridLayoutManager(
                context,
                4
            )
            adapter = categoryData
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}