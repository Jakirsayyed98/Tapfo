package app.tapho.RechargesNetwork

import androidx.lifecycle.ViewModel
import app.instagst.ui.interfaces.LoaderListener


class RechargesRequestViewModel : ViewModel() {
    private var loadLis: LoaderListener? = null

//    fun OperatorFatchAPI(
//        MOBILENUMBER: String?,
//        FORMAT: String?,
//        loadLis: LoaderListener?,
//        listener: ApiListener<OperatorFatchAPI, Any?>,
//    ) {
//        this.loadLis = loadLis
//      //  loadLis?.showLoader()
//        viewModelScope.launch(setErrorHandler(loadLis)) {
//            withContext(coroutineContext) {
//                MyRechargeApi().OperatorFatchAPI(MOBILENUMBER,FORMAT).body()
//                    ?.let {
//                        listener.onResponse(it, loadLis)
//                    }
//            }
//        }
//    }

//    fun AllRechargePlans(
//        Operator_Code: String?,
//        Circle_Code: String?,
//        MobileNumber: String?,
//        PageID: String?,
//        FORMAT: String?,
//        loadLis: LoaderListener?,
//        listener: ApiListener<getRechargePlans, Any?>,
//    ) {
//        this.loadLis = loadLis
//        loadLis?.showLoader()
//        viewModelScope.launch(setErrorHandler(loadLis)) {
//            withContext(coroutineContext) {
//                MyRechargeApi().AllRechargePlans(Operator_Code,Circle_Code,MobileNumber,PageID,FORMAT).body()
//                    ?.let {
//                        listener.onResponse(it, loadLis)
//                    }
//            }
//        }
//    }

}