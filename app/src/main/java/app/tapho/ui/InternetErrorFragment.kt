package app.tapho.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.tapho.R


/**
 * A simple [Fragment] subclass.
 * Use the [InternetErrorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InternetErrorFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_internet_error, container, false)
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
                if (frag is InternetErrorFragment && frag.isVisible)
                    frag.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun newInstance() =
            InternetErrorFragment()/*.apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/
    }
}