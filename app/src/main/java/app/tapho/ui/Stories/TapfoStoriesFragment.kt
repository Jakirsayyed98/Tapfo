package app.tapho.ui.Stories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import app.tapho.databinding.FragmentTapfoStoriesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.Stories.Model.Data
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.utils.DATA
import app.tapho.utils.parseDate
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory


class TapfoStoriesFragment : BaseFragment<FragmentTapfoStoriesBinding>(){

    private var storiesId = ""


    var Storiesdata : ArrayList<Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding1 = container?.let { FragmentTapfoStoriesBinding.inflate(inflater, it) }



        storiesId = activity?.intent?.getStringExtra(DATA).toString()
        setStoryDataViewModel(storiesId)




        return _binding?.root
    }

    private fun setStoryDataViewModel(storiesId: String) {
        viewModel.getStoriesData(getUserId(),this,object : ApiListener<StoriesResFile, Any?>{
            override fun onSuccess(t: StoriesResFile?, mess: String?) {
                t.let {
                    it!!.data.let {
                        it.forEach {
                            Storiesdata.add(it)
                        }
                        setStoryData(it)
                    }
                }
            }

        })
    }

    private fun setStoryData(it: List<Data>) {
        val myStories: ArrayList<MyStory> = ArrayList()
        for (story in Storiesdata) {
            myStories.add(MyStory(story.story.get(0).image))
        }

        StoryView.Builder(activity?.getSupportFragmentManager())
            .setStoriesList(myStories) // Required
            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
            .setTitleText("Hamza Al-Omari") // Default is Hidden
            .setSubtitleText("Damascus") // Default is Hidden
            .setTitleLogoUrl("some-link") // Default is Hidden
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {
                    //your action
                }

                override fun onTitleIconClickListener(position: Int) {
                    //your action
                }
            }) // Optional Listeners
            .build() // Must be called before calling show method
            .show()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TapfoStoriesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
            }
        }
        false
    }
}