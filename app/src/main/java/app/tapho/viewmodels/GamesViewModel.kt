package app.tapho.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.network.MyApi
import app.tapho.network.Resource
import app.tapho.ui.games.models.GamesCategoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesViewModel : ViewModel() {

    private val gamesCategory = MutableLiveData<Resource<List<GamesCategoryData>>>()

    fun getGamesCategories(userId: String?) {
        gamesCategory.postValue(Resource.loading(null))
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = MyApi().getGamezopCategory(userId)
                    if (response.isSuccessful)
                        gamesCategory.postValue(Resource.success(response.body()?.data))
                    else
                        gamesCategory.postValue(Resource.error(response.errorBody().toString(),
                            null))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getCategory(): LiveData<Resource<List<GamesCategoryData>>> {
        return gamesCategory
    }

}