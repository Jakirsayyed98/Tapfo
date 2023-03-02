package app.tapho.interfaces

import android.util.Log
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.network.BaseRes

interface ApiListener<T, U> {
    fun onSuccess(t: T?, mess: String?)
    fun onError(mess: String?) {}
    fun onResponse(t: T?, loaderListener: LoaderListener?) {
        loaderListener?.dismissLoader()
        if (t is BaseRes) {
            if (t.errorCode == "0") {
                 onSuccess(t, t.errorMsg)
            } else {
                if(t.type!="check_passcode") {
                    loaderListener?.showMess(t.errorMsg)
                }
                onError(t.errorMsg)

            }
        }else{
            onSuccess(t, "")
        }
    }
}