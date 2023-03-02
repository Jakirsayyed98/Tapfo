package app.tapho.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.FragmentSearchBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.home.adapter.searchUniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import java.util.*


class SearchDialogFragment : DialogFragment(), ApiListener<WebTCashRes, Any?>, LoaderListener,
    RecyclerClickListener {

    private val REQUEST_CODE_SPEECH_INPUT = 1

    private var binding: FragmentSearchBinding? = null
    private val handler = Handler(Looper.getMainLooper())
    private val receiver = Runnable {
        search()
    }
    private var viewModel: RequestViewModel? = null
    private var userId: String? = ""
    private var serviceAdapter: searchUniversalAdapter<MiniApp>? = null
    private var popularList: ArrayList<MiniApp> = ArrayList()
    private var job: Job? = null

    companion object {
        fun newInstance(type: String) =
            SearchDialogFragment().apply {
                arguments = Bundle().apply { putString("TYPE", type) }
            }
        fun showSearch(fragmentManager: FragmentManager, list: List<Popular>) {
            SearchDialogFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(DATA, Gson().toJson(list))
                    }
                }
                .show(fragmentManager, "SearchDialogFragment")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        dialog?.window?.statusBarColor =  ContextCompat.getColor(requireContext(), R.color.white)
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        init()
        return binding?.root
    }

    private fun init() {
        dismissLoader()
        binding?.backIv?.setOnClickListener { dismiss() }
        serviceAdapter = searchUniversalAdapter(this)
        binding?.recycler?.apply {
            layoutManager = GridLayoutManager(context, 4)
//            layoutManager = GridLayoutManager(context, 3 ,LinearLayoutManager.HORIZONTAL,false)
            adapter = serviceAdapter
        }

        Gson().fromJson<List<Popular>>(arguments?.getString(DATA),
            object : TypeToken<List<Popular>>() {}.type)?.let {
            it.forEach { pop ->
                pop.mini_app?.let { it1 -> popularList.add(it1) }
            }
        }

        popularList.let {
            serviceAdapter?.addAllItem(if (it.size >12) it.subList(0,12) else it)
        }
        userId = AppSharedPreference.getInstance(requireContext()).getUserId()
        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)

        binding!!.mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(context, " " + e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding?.searchEt?.doAfterTextChanged {
            binding!!.nomatch.visibility = View.GONE
            binding!!.progress.visibility = View.VISIBLE
            handler.removeCallbacks(receiver)
            handler.postDelayed(receiver, 700)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )

                handler.removeCallbacks(receiver)
                handler.postDelayed(receiver, 700)
                binding!!.searchEt.setText(Objects.requireNonNull(result)!!.get(0))
            }
        }
    }

    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(requireContext())
    }





    private fun search() {
        kotlin.runCatching {
            serviceAdapter?.clear()
            if (binding?.searchEt?.text.isNullOrEmpty()) {
                binding?.trendingIv?.visibility = View.VISIBLE
                binding?.progress?.visibility = View.GONE
                binding?.merchantsTv?.text = getString(R.string.trending_merchants)
                popularList.let {
                    serviceAdapter?.addAllItem(it)
                }
            } else {
                job?.cancel()
                job = viewModel?.searchMiniApp(userId, binding?.searchEt?.text.toString(), this, this)
            }
        }
    }





    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        t?.let {
            serviceAdapter?.clear()
            binding!!.progress.visibility = View.GONE
            binding?.trendingIv?.visibility = View.INVISIBLE
            binding?.merchantsTv?.text = getString(R.string.related_search)
            if (it.data.isNullOrEmpty()){
                binding!!.nomatch.visibility = View.VISIBLE
            }else{
                binding!!.nomatch.visibility = View.GONE
                serviceAdapter?.addAllItem(it.data)
            }
        }
    }

    override fun showLoader() {
        binding?.progress?.visibility = View.VISIBLE
    }

    override fun dismissLoader() {
        binding?.progress?.visibility = View.GONE
    }

    override fun showMess(mess: String?) {
        context?.toast(mess)
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW)
            if (data is MiniApp)
                ActiveCashbackForWebActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite,data.cashback,data.app_category_id,data.url_type,data.name) }
}