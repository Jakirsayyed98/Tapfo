package app.tapho.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import app.tapho.R

internal class KeyPadView : LinearLayout {
    private var textView: TextView? = null

    constructor(context: Context?) : super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val view = LayoutInflater.from(context).inflate(R.layout.keypad_layout, this,false)
        addView(view)

        view.findViewById<Button>(R.id.button_1).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_2).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_3).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_4).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_5).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_6).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_7).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_8).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_9).setOnClickListener { setText(it as Button) }
        view.findViewById<Button>(R.id.button_zero).setOnClickListener { setText(it as Button) }
        view.findViewById<View>(R.id.button_delete).setOnClickListener { deleteText() }
        view.findViewById<View>(R.id.button_delete).setOnLongClickListener { _ ->
            textView?.text = ""
            true
        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    fun setTextView(textView: TextView) {
        this.textView = textView
    }

    private fun setText(button: Button) {
        textView?.let {
            it.text = it.text.toString() + button.text
        }
    }

    private fun deleteText() {
        textView?.let {
            if (it.text.isNotEmpty())
                it.text = it.text.toString().substring(0, it.text.length - 1)
        }
    }
}