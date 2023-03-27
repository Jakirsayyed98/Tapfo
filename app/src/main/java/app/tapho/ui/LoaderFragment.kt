package app.tapho.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.tapho.R


class LoaderFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable=false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loader, container, false)
    }

    companion object {
        private const val TAG = "LoaderFragment"
        fun showLoader(fragmentManager: FragmentManager) {

            try {
                newInstance().show(fragmentManager, TAG)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun dismissLoader(fragmentManager: FragmentManager) {
            try {
                val frag = fragmentManager.findFragmentByTag(TAG)
                if (frag is LoaderFragment && frag.isVisible)
                    frag.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun newInstance() =
            LoaderFragment()
    }
}