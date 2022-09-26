package app.tapho.ui.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentOperatorCircleDialogBinding
import app.tapho.ui.recharge.adapter.OperatorCircleAdapter

class OperatorCircleDialogFragment : DialogFragment() {
    private var binding: FragmentOperatorCircleDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOperatorCircleDialogBinding.inflate(inflater, container, false)

        binding?.closeIv?.setOnClickListener {
            dismiss()
        }
        setRecycler()
        return binding?.root
    }

    private fun setRecycler() {
        binding?.recyclerCircle?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OperatorCircleAdapter()
        }
    }
}