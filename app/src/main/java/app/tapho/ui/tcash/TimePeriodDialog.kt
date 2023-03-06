package app.tapho.ui.tcash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.DialogTimePeriodBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.model.TimeModel
import app.tapho.ui.tcash.adapter.TimeAdapter
import app.tapho.utils.appLog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TimePeriodDialog : BottomSheetDialogFragment(), RecyclerClickListener {
    private var _binding: DialogTimePeriodBinding? = null
    private var mAdapter: TimeAdapter? = null

    companion object {
        private val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        fun getCurrentDate(): String {
            return outputDateFormat.format(Calendar.getInstance().time)
        }

        fun getDate(day: Int, month: Int): String {
            return outputDateFormat.format(Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, day)
                add(Calendar.MONTH, month)
            }.time)
        }

        fun addDays(day: Int): String {
            return outputDateFormat.format(Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, day)
            }.time)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogTimePeriodBinding.inflate(inflater, container, false)
        setRecycler()
        _binding?.closeIv?.setOnClickListener { dismiss() }
        return _binding?.root
    }

    private fun setRecycler() {
        mAdapter = TimeAdapter(this)
        mAdapter?.addItem(TimeModel(getString(R.string.today), false))
        mAdapter?.addItem(TimeModel(getString(R.string.yesterday), false))
        mAdapter?.addItem(TimeModel(getString(R.string.last_7_days), false))
        mAdapter?.addItem(TimeModel(getString(R.string.this_month), false))
        mAdapter?.addItem(TimeModel(getString(R.string.last_month), false))
        mAdapter?.addItem(TimeModel(getString(R.string.last_90_days), false))
        mAdapter?.addItem(TimeModel(getString(R.string.last_6_month), false))
        mAdapter?.addItem(TimeModel(getString(R.string.till_date), false))
        _binding?.recyclerTime?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = mAdapter
        }
        arguments?.getString("TIME")?.let {
            mAdapter?.select(it)
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        var startDate = ""
        var endDate = ""
        if (pos == 0) {
            startDate = getCurrentDate()
            endDate = getCurrentDate()
        } else if (pos == 1) {
            startDate = addDays(-1)
            endDate = addDays(-1)
        } else if (pos == 2) {
            startDate = addDays(-7)
            endDate = getCurrentDate()
        } else if (pos == 3) {
            startDate = getDate(1, 0)
            endDate = getCurrentDate()
        } else if (pos == 4) {
            startDate = getDate(1, -1)
            endDate = getDate(31, -1)
        } else if (pos == 5) {
            startDate = addDays(-90)
            endDate = getCurrentDate()
        } else if (pos == 6) {
            startDate = getDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), -6)
            endDate = getCurrentDate()
        } else if (pos == 7) {
            startDate = getDate(1, -12)
            endDate = getCurrentDate()
        }
        if (parentFragment is Fragment_wallet_history) {
            (parentFragment as Fragment_wallet_history).getData(startDate,
                endDate,
                (data as TimeModel).time)
        }
        if (parentFragment is TcashrewardsFragment) {
            (parentFragment as TcashrewardsFragment).getData(startDate, endDate, (data as TimeModel).time)
        }

        if (parentFragment is Fragment_Tcash_HistoryData) {
            (parentFragment as Fragment_Tcash_HistoryData).getData(startDate,
                endDate,
                (data as TimeModel).time)
        }

        if (parentFragment is AllTransactionFragment) {
            (parentFragment as AllTransactionFragment).getData(startDate,
                endDate, (data as TimeModel).time)
        }
        dismiss()
       // appLog((data as TimeModel).time + "  " + startDate + "  " + endDate)
        appLog((data as TimeModel).time + "  " + startDate + "  " + endDate)
    }

}