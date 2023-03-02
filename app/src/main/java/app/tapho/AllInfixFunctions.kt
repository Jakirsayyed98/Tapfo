package app.tapho

import android.content.*
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


infix fun View.showShortSnack(message: String) {
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}

 fun View.showCustomeSnack(newview:View ) {
    val snackbar = Snackbar.make(this, "", Snackbar.LENGTH_LONG)

    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackbarLayout = snackbar.view as SnackbarLayout
    snackbarLayout.setPadding(0, 0, 0, 0)
    snackbarLayout.addView(newview)
    snackbar.show()

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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+91"+number+"?text=Hiii"))
        startActivity(intent)
    }else{
        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
    }
}


@SuppressWarnings("deprecation")
infix fun Context.HtmlToText(text: String): Spanned? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
    } else {
        return Html.fromHtml(text);
    }
}




infix fun Context.CamelCaseValue(text : String) : String{

    val Words = text.drop(1)
    text.toCharArray()
    val data =text.get(0).uppercase()+ Words
    return data
}

infix fun Context.openMailBox(emailId: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.setData(Uri.parse("mailto:"))
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_EMAIL, emailId)
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"")
    intent.putExtra(android.content.Intent.EXTRA_TEXT, "")
    this.startActivity(Intent.createChooser(intent, "Send Email"))
}



infix fun Context.DoubleRoundFiger(amount : String): String{
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
            override fun updateDrawState(ds: TextPaint) { ds.typeface = Typeface.create(ResourcesCompat.getFont(this@SpannableStringS2, R.font.quicksand_bold), Typeface.NORMAL) } }, 0, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
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


infix fun Context.bitmapImage(image :Int) : Bitmap?{

    val mIcon = BitmapFactory.decodeResource(this.getResources(), image)

    return mIcon
}


infix fun Context.addRupeesSign(named : String) : String?{


    return named
}


infix fun Context.getExprieOrNot(dateofExpire : String): String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar1: Calendar = Calendar.getInstance()
    val calendar2: Calendar = Calendar.getInstance()

    val date1: Date = Calendar.getInstance().time
    val EndDate: Date = dateFormat.parse(dateofExpire!!)

    calendar1.setTime(date1)
    calendar2.setTime(EndDate)
        print("Compare Result :Expire" +dateofExpire)
    if (EndDate.after(date1)){
        print("Compare Result :Active\t $date1\t $EndDate" )
    }else{
        print("Compare Result :Expire\t $date1\t $EndDate" )
    }

    return dateofExpire
}






