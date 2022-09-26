package app.tapho.utils

import android.annotation.SuppressLint
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.graphics.Typeface
import android.text.*
import android.text.format.DateUtils
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.interfaces.SpanClickListener
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


fun Context.toast(mess: String?) {
//     Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
}

fun setErrorHandler(loadLis: LoaderListener?): CoroutineExceptionHandler {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        loadLis?.dismissLoader()
        if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            loadLis?.showMess("Internet error")
        } else {
            loadLis?.showMess(throwable.message)
        }
    }
    return exceptionHandler
}


fun appLog(mess: String?) {
    Log.e("TAPFO", mess.toString())
}

fun getUniqueCode(): String {
    return (Math.random() * 10.0.pow(10.0)).toInt().toString()
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun getCustomTab(context: Context?, title: String?): View? {
    return LayoutInflater.from(context).inflate(R.layout.layout_custom_tab, null)?.apply {
        findViewById<TextView>(R.id.titleTv).text = title
    }
}

fun Context.customToast(mess: String?, isError: Boolean) {
    try {
        Toast(this).apply {
            view =
                LayoutInflater.from(this@customToast).inflate(R.layout.toast_layout, null).apply {
                    findViewById<TextView>(R.id.toastTv).text = mess
                    backgroundTintList = ContextCompat.getColorStateList(
                        this@customToast,
                        if (isError) R.color.red else R.color.green_light
                    )
                }
            duration = Toast.LENGTH_SHORT
        }.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun withSuffixAmount(amount: String?): String? {
    try {
        amount?.let {
            val count = amount.toDouble()
            return String.format("₹%.2f", count)//.replaceAfter(".","").replace(".","")
            /* if (count < 1000) return String.format("₹%.2f", count)
             val exp = (ln(count) / ln(1000.0)).toInt()
             return String.format("₹%.1f %c",
                 count / 1000.0.pow(exp.toDouble()),
                 "kMGTPE"[exp - 1])*/
        }
        return "₹$amount"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "₹$amount"
}
fun withSuffixAmount3(amount: String?): String? {
    try {
        amount?.let {
            val count = amount.toDouble()
            return String.format("%.2f", count)
            /* if (count < 1000) return String.format("₹%.2f", count)
             val exp = (ln(count) / ln(1000.0)).toInt()
             return String.format("₹%.1f %c",
                 count / 1000.0.pow(exp.toDouble()),
                 "kMGTPE"[exp - 1])*/
        }
        return "$amount"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "$amount"
}

fun withSuffixAmount2(amount: String?): String? {
    try {
        amount?.let {
            val count = amount.toDouble()
            return String.format("%.2f", count)
            /* if (count < 1000) return String.format("₹%.2f", count)
             val exp = (ln(count) / ln(1000.0)).toInt()
             return String.format("₹%.1f %c",
                 count / 1000.0.pow(exp.toDouble()),
                 "kMGTPE"[exp - 1])*/
        }
        return "$amount".replaceAfter(".","").replace(".","")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "$amount".replaceAfter(".","").replace(".","")
}

fun roundOff(amount: String?): String {
    amount?.let {
        try {
            val count = amount.toDouble()
            if (count < 1000) return String.format("₹%.2f", count)
            val exp = (ln(count) / ln(1000.0)).toInt()
            return String.format(
                "₹%.1f %c",
                count / 1000.0.pow(exp.toDouble()),
                "kMGTPE"[exp - 1]
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return ""
}



fun getSpannableCashbackText(s: String, @ColorInt color: Int): SpannableString {
    var text = s
    try {
        text = URLDecoder.decode(text, "UTF-8")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val split = text.split(" ")
    if (split.size > 2)
        return SpannableString(text).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                split[0].length + 1,
                text.length - split[2].length - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(color), split[0].length + 1,
                text.length - split[2].length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    return SpannableString(text)
}

fun getSpannableBold(
    s: String,
    startPos: Int,
    endPos: Int,
    color: Int?,
    fontSize: Float,
): SpannableStringBuilder {
    return SpannableStringBuilder(s).apply {
        setSpan(
            StyleSpan(Typeface.BOLD), startPos,
            if (endPos == -1) length else endPos,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        if (color != null)
            setSpan(
                ForegroundColorSpan(color),
                startPos,
                if (endPos == -1) length else endPos,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

        if (fontSize != 0f)
            setSpan(RelativeSizeSpan(fontSize), startPos, if (endPos == -1) length else endPos, 0)
    }
}

fun setClickableSpannable(
    s: String,
    startPos: Int,
    endPos: Int,
    color: Int?,
    spanClickListener: SpanClickListener,
): SpannableStringBuilder {
    return SpannableStringBuilder(s).apply {
        setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {
                    spanClickListener.onSpanClick()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    if (color != null) {
                        ds.color = color
                    }
                }
            }, startPos,
            if (endPos == -1) length else endPos,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
}

fun parseDate(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("yy-MM-dd HH:mm", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseyear(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("yyyy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

@SuppressLint("SimpleDateFormat")
fun parseAgoDate(date: String?): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var finaldate = ""
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val time = sdf.parse(date).time
        val now = System.currentTimeMillis()
        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
        finaldate = ago.toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return finaldate
}

fun parseDate2(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseDateWithFullFormate(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
     //           return SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.ENGLISH).format(it)
                return SimpleDateFormat("hh:mm a ,dd MMM yyyy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}



fun parseDate3(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseDatewithmonth(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun onlyDatewithMonth(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseDate4(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd MMM yy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseDate5(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd MMM yy | hh:mm a", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun finalDatewithAMPM(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd MMM yy hh:mm a", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parsetime3(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}
fun parseDate6(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("MMM dd, yyyy at hh:mm a", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseOnlyDate(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd MMM", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun parseTime(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}


fun parseData2(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

fun getDate(date: String): Date? {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)
}

