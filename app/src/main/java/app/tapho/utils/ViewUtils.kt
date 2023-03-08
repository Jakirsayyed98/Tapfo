package app.tapho.utils


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.location.LocationManagerCompat
import androidx.palette.graphics.Palette
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.TapfoApplication.Companion.applicationContext
import app.tapho.interfaces.SpanClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.datatransport.runtime.dagger.Provides
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.*
import okio.Buffer
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
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
//    Log.e("TAPFO", mess.toString())
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

fun getCustomTab2(context: Context?, title: String?): View? {
    return LayoutInflater.from(context).inflate(R.layout.layout_custom_tab1, null)?.apply {
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


@SuppressLint("HardwareIds")
fun Context.getSystemDetail(): String {
    return "Brand: ${Build.BRAND} \n" +
            "DeviceID: ${
                Settings.Secure.getString(
                    this.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            } \n" +
            "Model: ${Build.MODEL} \n" +
            "ID: ${Build.ID} \n" +
            "SDK: ${Build.VERSION.SDK_INT} \n" +
            "Manufacture: ${Build.MANUFACTURER} \n" +
            "Brand: ${Build.BRAND} \n" +
            "User: ${Build.USER} \n" +
            "Type: ${Build.TYPE} \n" +
            "Base: ${Build.VERSION_CODES.BASE} \n" +
            "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
            "Board: ${Build.BOARD} \n" +
            "Host: ${Build.HOST} \n" +
            "FingerPrint: ${Build.FINGERPRINT} \n" +
            "Version Code: ${Build.VERSION.RELEASE}"
}


@SuppressLint("HardwareIds")
fun Context.getSystemDeviceID(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

}


fun getGreetingMessage(): String {
    val c = Calendar.getInstance()
    val hours = c.get(Calendar.HOUR_OF_DAY)

    return when (hours) {
        in 4..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        in 21..24 -> "Good Evening"
        else -> "\uD83D\uDE34"
    }
}

fun getGreetingForRestro(): String {
    val c = Calendar.getInstance()
    val hours = c.get(Calendar.HOUR_OF_DAY)

    return when (hours) {
        in 0..2 -> "Dinner"
        in 2..4 -> "breakfast "
        in 4..12 -> "breakfast "
        in 12..16 -> "lunch"
        in 16..20 -> "snacks"
        in 20..24 -> "dinner"
        else -> "break"
    }
}

fun Context.getOpretordata(opretorID: String): String {
    val data = AppSharedPreference.getInstance(this).getString("ServiceOpretor" + opretorID)
    return data.toString()
}

fun Context.getCircleData(opretorID: String): String {
    val data = AppSharedPreference.getInstance(this).getString("ServiceCircle" + opretorID)
    return data.toString()
}


fun setGlideImageDataNormal(context: Context, icon: ImageView, imageURL: String) {
    Glide.with(context).load(imageURL).placeholder(R.drawable.loding_app_icon).into(icon)
}

fun setGlideImageDataCircleCrop(context: Context, icon: ImageView, imageURL: String) {
    Glide.with(context).load(imageURL).circleCrop().placeholder(R.drawable.loding_app_icon)
        .into(icon)
}

fun setGlideImageDataCenterCrop(context: Context, icon: ImageView, imageURL: String) {
    Glide.with(context).load(imageURL).centerCrop().placeholder(R.drawable.loding_app_icon)
        .into(icon)
}

fun setGlidewithbitmap(context: Context, imageURL: String, icon: ImageView): Int {
    var colorcode: Int = R.color.grey_dark
    Glide.with(context).asBitmap().load(imageURL).centerCrop().into(object :
        BitmapImageViewTarget(icon) {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            super.onResourceReady(resource, transition)
            Palette.from(resource).generate().vibrantSwatch?.rgb?.let {
                colorcode = it
            }
        }
    })

    return colorcode
}

fun creatPaletteSync(bitmap: Bitmap): Int {
    val let = Palette.from(bitmap).generate().vibrantSwatch?.rgb?.let {
        return it
    }
    return R.color.blue_00ade6
}


fun Context.customToastForSupport(mess: String?, isError: Boolean) {
    try {
        Toast(this).apply {
            view =
                LayoutInflater.from(this@customToastForSupport)
                    .inflate(R.layout.support_toast_layout, null).apply {
                    findViewById<TextView>(R.id.toastTv).text = mess
                    backgroundTintList = ContextCompat.getColorStateList(
                        this@customToastForSupport,
                        if (isError) R.color.red else R.color.green_dark
                    )
                }
            duration = Toast.LENGTH_SHORT
        }.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
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
            val count = amount//.toDouble()
            return String.format("₹%.2f", count)//.replaceAfter(".","").replace(".","")
            /* if (count < 1000) return String.format("₹%.2f", count)
             val exp = (ln(count) / ln(1000.0)).toInt()
             return String.format("₹%.1f %c",
                 count / 1000.0.pow(exp.toDouble()),
                 "kMGTPE"[exp - 1])*/
        }
        return "₹$amount".replaceAfter(".", "").replace(".", "")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "₹$amount".replaceAfter(".", "").replace(".", "")
}

fun withSuffixAmount4(amount: String?): String? {
    try {
        amount?.let {
            val count = amount//.toDouble()
            return String.format("₹%.2f", count)//.replaceAfter(".","").replace(".","")
            /* if (count < 1000) return String.format("₹%.2f", count)
             val exp = (ln(count) / ln(1000.0)).toInt()
             return String.format("₹%.1f %c",
                 count / 1000.0.pow(exp.toDouble()),
                 "kMGTPE"[exp - 1])*/
        }
        return "₹$amount".replaceAfter(".", "").replace(".", "")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "₹$amount".replaceAfter(".", "").replace(".", "")
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

fun roundOffAmount(amount: String?): String {
    amount?.let {
        try {
            val count = amount.toDouble()
            if (count < 1000) return String.format("%.2f", count)
            val exp = (ln(count) / ln(1000.0)).toInt()
            return String.format(
                "%.1f %c",
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


fun getSpannableCashbackTextColor(s: String, @ColorInt color: Int): SpannableString {
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

fun parseDateExpire(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("dd MMM yyy", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(it)
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
        val time = sdf.parse(date)?.time
        val now = System.currentTimeMillis()
        val ago = DateUtils.getRelativeTimeSpanString(time!!, now, DateUtils.MINUTE_IN_MILLIS)
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

fun onlyDatewithMonth2(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.let {
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

fun parseDate4StartMonth(date: String?): String? {
    try {
        if (date.isNullOrEmpty().not())
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)?.let {
                return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(it)
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

fun decodeCashback(cashback: String?): String? {
    return URLDecoder.decode(cashback, "UTF-8")
}

fun LogAppData(mess: String?) {

}

fun Context.rateApp() {
    val uri: Uri =
        Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext().packageName.toString() + "")
    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
    try {
        startActivity(myAppLinkToMarket)
    } catch (e: ActivityNotFoundException) {
        this.toast("Unable to find market app")
    }
}

fun getSecretKeyFromString(key: ByteArray): SecretKey {
    return SecretKeySpec(key, 0, key.size, "AES")
}

/*
fun encrypt(strToEncrypt: String): String {
    val SECRET_KEY = "6d66fb7debfd15bf716bb14752b9603b".toByteArray()
    val iv ="1234567890123456".toByteArray()
    val ivspec = IvParameterSpec(iv)
    val tmp: SecretKey = getSecretKeyFromString(SECRET_KEY)
    val secretKey = SecretKeySpec(tmp.encoded,0,32, "AES")

    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
    val encodeddata = cipher.doFinal(strToEncrypt.toString().toByteArray())
    val encode = android.util.Base64.encodeToString(encodeddata, android.util.Base64.NO_WRAP)

    return encode
}


fun decrypt(strToDecrypt: String?): String? {
    try {
        val SECRET_KEY = "6d66fb7debfd15bf716bb14752b9603b".toByteArray()
//        val iv =")Q+{b@&<*%#@%$%^".toByteArray()
        val iv ="1234567890123456".toByteArray()
        val ivspec = IvParameterSpec(iv)


        val tmp: SecretKey = getSecretKeyFromString(SECRET_KEY)
        val secretKey = SecretKeySpec(tmp.encoded,0,32, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec)
        return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return e.toString()+"1"
    }
    return null
}

*/


external fun SecretKey(): String?
external fun IVKey(): String?
fun encrypt(strToEncrypt: String): String {

    System.loadLibrary("keys")

    val SECRET_KEY = SecretKey()!!.toByteArray()
    val iv = IVKey()!!.toByteArray()
    val ivspec = IvParameterSpec(iv)
    val tmp: SecretKey = getSecretKeyFromString(SECRET_KEY)
    val secretKey = SecretKeySpec(tmp.encoded, 0, 32, "AES")

    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
    val encodeddata = cipher.doFinal(strToEncrypt.toString().toByteArray())
    val encode = android.util.Base64.encodeToString(encodeddata, android.util.Base64.NO_WRAP)

    return encode
}


fun decrypt(strToDecrypt: String?): String? {
    try {
        System.loadLibrary("keys")


        val SECRET_KEY = SecretKey()!!.toByteArray()
        val iv = IVKey()!!.toByteArray()
        val ivspec = IvParameterSpec(iv)


        val tmp: SecretKey = getSecretKeyFromString(SECRET_KEY)
        val secretKey = SecretKeySpec(tmp.encoded, 0, 32, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec)
        return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return e.toString() + "1"
    }
    return null
}


fun encrypttest(strToEncrypt: String, SECRET_KEY: SecretKey, IV: ByteArray): String {

    val ivspec = IvParameterSpec(IV)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, ivspec)
    val encodeddata = cipher.doFinal(strToEncrypt.toString().toByteArray())
    val encode = android.util.Base64.encodeToString(encodeddata, android.util.Base64.NO_WRAP)

    return encode
}

fun decrypttest(strToDecrypt: String?, SECRET_KEY: SecretKey, IV: ByteArray): String? {
    try {

        val ivspec = IvParameterSpec(IV)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, ivspec)
        return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return e.toString()
    }
    return null
}

fun bodyToString(request: RequestBody): String {
    return try {
        val buffer = Buffer()
        request.writeTo(buffer)
        buffer.readUtf8()
    } catch (e: IOException) {
        "did not work"
    }
}


fun Context.setOnCustomeCrome(url: String) {
    val customIntent = CustomTabsIntent.Builder()
    customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.white))
    val backbtn = BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_back_black_24dp)
    customIntent.setCloseButtonIcon(backbtn)
    customIntent.setDefaultShareMenuItemEnabled(false)
    customIntent.build()
    openCustomTab(this, customIntent.build(), Uri.parse(url))
}

private fun openCustomTab(context: Context, customTabsIntent: CustomTabsIntent, Url: Uri) {
    val packageName = "com.android.chrome"
    customTabsIntent.intent.setPackage(packageName)
    customTabsIntent.launchUrl(context, Url)

}


fun hasNetwork(context: Context): Boolean? {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}


fun CacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        var request: Request = chain.request()
        val originalResponse: Response = chain.proceed(request)

        val cacheControl: String? = originalResponse.header("Cache-Control")

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")
        ) {
            val cc = CacheControl.Builder()
                .maxStale(1, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .removeHeader("Pragma")
                .cacheControl(cc)
                .build()
            chain.proceed(request)

        } else {
            originalResponse

        }
    }
}


fun OfflineCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        try {
            return@Interceptor chain.proceed(chain.request())
        } catch (e: Exception) {
            val cacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(1, TimeUnit.DAYS)
                .build()
            val offlineRequest: Request = chain.request().newBuilder()
                .cacheControl(cacheControl)
                .removeHeader("Pragma")
                .build()
            return@Interceptor chain.proceed(offlineRequest)
        }
    }
}









