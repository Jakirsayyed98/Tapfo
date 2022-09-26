package app.tapho.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.network.MyApi
import app.tapho.network.Resource
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.Data
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentGameViewModel : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Resource.error(throwable.message, null)
    }

    private val returnList = MutableLiveData<Resource<List<Data>>>()

    fun getGameRecentPlayList(userId: String) {
        returnList.postValue(Resource.loading(null))
        try {
            viewModelScope.launch(exceptionHandler) {
                withContext(Dispatchers.IO) {
                    val response = MyApi().getGameRecentList(userId)
                    if (response.isSuccessful){
                        returnList.postValue(Resource.success(response.body()?.data))
                    Log.d(
                        "Data", "${returnList.postValue(Resource.success(response.body()?.data))}"
                    )
                }else
                        returnList.postValue(Resource.error(response.errorBody().toString(), null))
                }
            }
        } catch (e: Exception) {
            returnList.postValue(Resource.error(e.message, null))
        }
    }

    fun getData(): LiveData<Resource<List<Data>>> {
        return returnList
    }
}