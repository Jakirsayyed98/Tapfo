package app.tapho.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.tapho.R
import app.tapho.databinding.DialogAllowLocationBinding

class AllowLocationDialog : DialogFragment() {
    private var _binding: DialogAllowLocationBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            AllowLocationDialog().show(fragmentManager, AllowLocationDialog::class.java.name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAllowLocationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notNowTv.setOnClickListener { dismiss() }
        binding.allowLocationBtn.setOnClickListener {
            startActivity(Intent(context, SetUpProfileActivity::class.java))
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}