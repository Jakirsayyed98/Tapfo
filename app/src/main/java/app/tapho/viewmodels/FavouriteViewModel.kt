package app.tapho.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.network.Resource
import app.tapho.network.MyApi
import app.tapho.network.MyApiV2
import app.tapho.ui.model.MiniApp
import app.tapho.utils.encrypt
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Resource.error(throwable.message, null)
    }

    private val returnList = MutableLiveData<Resource<List<MiniApp>>>()

    fun getFavouriteList(userId: String) {
        returnList.postValue(Resource.loading(null))
        val req = JsonObject().apply {
            addProperty("user_id",userId)
        }
        try {
            viewModelScope.launch(exceptionHandler) {
                withContext(Dispatchers.IO) {
                    val response = MyApiV2().getFavMiniApp(encrypt(req.toString()))
                    if (response.isSuccessful)
                        returnList.postValue(Resource.success(response.body()?.data))
                    else
                        returnList.postValue(Resource.error(response.errorBody().toString(), null))
                }
            }
        } catch (e: Exception) {
            returnList.postValue(Resource.error(e.message, null))
        }
    }

    fun getData(): LiveData<Resource<List<MiniApp>>> {
        return returnList
    }
}