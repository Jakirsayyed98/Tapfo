package app.tapho

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.palette.graphics.Palette
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.pow


infix fun View.showShortSnack(message: String) {
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}

infix fun Context.copyElement(textData : String){
    val textToCopy = textData
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", textToCopy)
    clipboardManager.setPrimaryClip(clipData)
}


infix fun Context.DirectCall(number : String){
    val i = Intent(Intent.ACTION_DIAL)
    val p = "tel:" + number
    i.data = Uri.parse(p)
    startActivity(i)
}


infix fun Context.ContactOnWhatsapp(number: String){
    val pm: PackageManager =this.getPackageManager()
    var isInstalled : Boolean
    try {
        val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
        isInstalled = true
    }catch (e : PackageManager.NameNotFoundException){
        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
        isInstalled = false

    }

    if (isInstalled==true){
        // val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?.phone=8108893686&text=I'm%20interested%20in%20your%20car%20for%20sale"))
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+91"+8108893686+"?text=I'm%20interested%20in%20your%20car%20for%20sale"))
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+91"+number+"?text=I'm%20interested%20in%20your%20car%20for%20sale"))
        startActivity(intent)
    }else{
        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
    }
}


infix fun Context.CamelCaseValue(text : String) : String{

    val Words = text.drop(1)
    text.toCharArray()
    val data =text.get(0).uppercase()+ Words
    return data

}


infix fun Context.DoubleRonduFiger(amount : String): String{
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

    return amount
}

infix fun Context.SpannableStringS1(Data : String): String{

    val s = SpannableString(Data).apply {
        setSpan(object : TypefaceSpan(null) {
            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = Typeface.create(
                    ResourcesCompat.getFont(this@SpannableStringS1,
                    R.font.quicksand_bold), Typeface.NORMAL)
            }
        }, 0, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    return s.toString()
}

infix fun Context.SpannableStringS2(Data : String): String{

    val s = SpannableString(Data).apply {
        setSpan(object : TypefaceSpan(null) {
            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = Typeface.create(
                    ResourcesCompat.getFont(this@SpannableStringS2,
                    R.font.quicksand_bold), Typeface.NORMAL)
            }
        }, 0, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    return s.toString()
}


infix fun Context.TotalPlaySuffixFunction(count: Double) :Double {
    val df = DecimalFormat("#.#")
     if (Math.abs(count / 1000000) >= 1) {
        df.format(count / 1000000.0).toString() + "M Plays"
    } else if (Math.abs(count / 1000.0) >= 1) {
        df.format(count / 1000.0).toString() + "K Plays"
    } else {
        count.toString()+" Plays"
    }
    return count
}


private val Context.viewLifecycleOwner: LifecycleOwner
    get() {
        TODO("Not yet implemented")
    }