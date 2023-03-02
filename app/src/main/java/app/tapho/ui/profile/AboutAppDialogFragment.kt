package app.tapho.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.tapho.BuildConfig
import app.tapho.R

class AboutAppDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.versionTv).text = BuildConfig.VERSION_NAME
    }

    companion object {
        @JvmStatic
        fun show(fragmentManager: FragmentManager) =
            AboutAppDialogFragment().show(fragmentManager, "")
    }
}