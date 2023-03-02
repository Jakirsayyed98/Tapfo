package app.tapho.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.network.Resource
import app.tapho.network.MyApi
import app.tapho.network.MyApiV2
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.utils.encrypt
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MerchantsAllListViewModel : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Resource.error(throwable.message, null)
    }

    private val data = MutableLiveData<Resource<CashbackMerchantsAllRes?>>()

    fun getCashbackMerchants(userId: String?, merchantCategoryId: String?) {
        val req = JsonObject().apply {
            addProperty("user_id",userId)
            addProperty("cashback_merchant_category_id",merchantCategoryId)
        }
        data.postValue(Resource.loading(null))
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val res = MyApiV2().getCashbackMerchant(encrypt(req.toString()))
                    if (res.isSuccessful) {
                        data.postValue(Resource.success(res.body()))
                    } else
                        data.postValue(Resource.error(res.message(), null))
                } catch (e: Exception) {
                    e.printStackTrace()
                    data.postValue(Resource.error(e.message, null))
                }
            }
        }
    }


    fun getValue(): LiveData<Resource<CashbackMerchantsAllRes?>> {
        return data
    }

}