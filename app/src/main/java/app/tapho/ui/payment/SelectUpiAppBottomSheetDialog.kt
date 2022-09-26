package app.tapho.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.DialogSelectUpiDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.one97.paytm.nativesdk.instruments.upicollect.models.UpiOptionsModel

class SelectUpiAppBottomSheetDialog : BottomSheetDialogFragment() {
    private var binding: DialogSelectUpiDialogBinding? = null

    companion object {
        fun newInstance(
            upiModels: ArrayList<UpiOptionsModel>,
            amount: Double,
        ): SelectUpiAppBottomSheetDialog {
            val args = Bundle()
            args.putParcelableArrayList("MODELS", upiModels)
            args.putDouble("AMOUNT", amount)
            val fragment = SelectUpiAppBottomSheetDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogSelectUpiDialogBinding.inflate(inflater, container, false)
        init()
        return binding?.root
    }

    private fun init() {
        val models= arguments?.getParcelableArrayList<UpiOptionsModel>("MODELS")
        if(models.isNullOrEmpty().not()){
            binding?.recyclerUpi?.apply {
                layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                adapter=UpiAppAdapter().apply {
                    models?.let { addAllItem(it) }
                }
            }
        }
    }

}