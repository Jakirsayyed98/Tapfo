package app.tapho

import android.content.Context
import android.content.SharedPreferences
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

fun setFullScreen(window:Window){
    WindowCompat.setDecorFitsSystemWindows(window,false)
}
fun lightstatusBar(window:Window,isLight:Boolean=true,isLightNav:Boolean=false) {

    val wic=WindowInsetsControllerCompat(window,window.decorView)
    wic.isAppearanceLightStatusBars=isLight
    wic.isAppearanceLightNavigationBars=isLightNav
}


public inline infix fun<T> Iterable<T>.forEach(action: (T) -> Unit) {
    for (element in this) action(element)
}

public inline infix fun<T> Iterable<T>.forEachIndexed(action: (index: Int, T) -> Unit) {
    var index: Int = 0
    for (element in this) action(index++, element)
}

public infix fun<T> Iterable<T>.lastIndexOf(element: T): Int {
    (this as? List)?.lastIndexOf(element)
    var lastIndex = -1
    var index = 0
    for (item in this) {
        if (element == item) lastIndex = index
        index++
    }
    return lastIndex
}

public infix fun<T> List<T>.indexOf(element: T): Int {
    return indexOf(element)
}

public infix fun<T> Iterable<T>.indexOf(element: T): Int {
    (this as? List)?.indexOf(element)
    var index = 0
    for (item in this) {
        if (element == item) return index
        index++
    }
    return -1
}

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}


infix fun SharedPreferences.Editor.makeString(pair: Pair<String, String>) = this.putString(pair.first, pair.second)

infix fun SharedPreferences.Editor.saveInt(pair: Pair<String, Int>) = this.putInt(pair.first, pair.second)


infix fun SharedPreferences.Editor.saveFloat(pair: Pair<String, Float>) = this.putFloat(pair.first, pair.second)


infix fun SharedPreferences.Editor.saveLong(pair: Pair<String, Long>) = this.putLong(pair.first, pair.second)


infix fun SharedPreferences.Editor.saveBoolean(pair: Pair<String, Boolean>) = this.putBoolean(pair.first, pair.second)

infix fun SharedPreferences.valueOf(key: String) = this.getString(key, "")

infix fun SharedPreferences.inValueOf(key: String) = this.getInt(key, 0)

infix fun SharedPreferences.floatValueOf(key: String) = this.getFloat(key, 0f)

infix fun SharedPreferences.longValueOf(key: String) = this.getLong(key, 0L)

infix fun SharedPreferences.boolValueOf(key: String) = this.getBoolean(key, false)

infix fun SharedPreferences.Editor.remove(key: String) = this.remove(key)

infix fun View.onclick(f: (View) -> Unit) {
    this.setOnClickListener(f)
}

infix fun View.onlongClick(f: (View) -> Boolean) {
    this.setOnLongClickListener(f)
}

infix fun View.ontouch(f: (View, MotionEvent) -> Boolean) {
    this.setOnTouchListener(f)
}

infix fun View.keyevent(f: (View, Int, KeyEvent) -> Boolean) {
    this.setOnKeyListener(f)
}

infix fun View.focuschange(f: View.OnFocusChangeListener) {
    this.setOnFocusChangeListener(f)
}

infix fun CompoundButton.checkchanged(f: (CompoundButton, Boolean) -> Unit) {
    this.setOnCheckedChangeListener(f)
}

infix fun AdapterView<*>.itemclicked(f: (AdapterView<*>, View, Int, Long) -> Unit) {
    this.setOnItemClickListener(f)
}

infix fun <T: AbsListView>T.scrollchange(statechange: (View, Int) -> Unit) {
    val listener = object: AbsListView.OnScrollListener {
        override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        }

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            statechange(view, scrollState)
        }

    }
    this.setOnScrollListener(listener)
}

infix fun Context.showShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

infix fun Context.showLong(message: String) {
//    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}