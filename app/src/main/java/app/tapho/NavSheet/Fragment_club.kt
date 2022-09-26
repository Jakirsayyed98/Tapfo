package app.tapho.NavSheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R

// TODO: Rename parameter arguments, choose names that match

class Fragment_club : Fragment() {
    // TODO: Rename and change types of parameters


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
        return inflater.inflate(R.layout.fragment_club, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            Fragment_club().apply {
                arguments = Bundle().apply {

                }
            }
    }
}