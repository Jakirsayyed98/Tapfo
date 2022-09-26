package app.tapho.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.network.Resource
import app.instagst.ui.interfaces.LoaderListener2
import app.tapho.network.BaseRes
import app.tapho.network.MyApi
import app.tapho.ui.model.MiniApp
import app.tapho.utils.setErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CategoriesDataViewModel : ViewModel() {

    private val list = MutableLiveData<Resource<List<MiniApp>>>()

    fun getData(): LiveData<Resource<List<MiniApp>>> {
        return list
    }

    fun getCategoryList(userid: String?, app_category_id: String?) {
        list.postValue(Resource.loading(null))
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            list.postValue(Resource.error(throwable.message, null))
        }) {
            withContext(Dispatchers.IO) {
                val response = MyApi().getAppCategoryMiniApp(userid, app_category_id).body()
                if (response != null && response.errorCode == "0") {
                    list.postValue(Resource.success(response.mini_app))
                } else
                    list.postValue(Resource.error("Error", null))
            }
        }
    }

    fun makeFev(
        pos: Int,
        type: String,
        userId: String?,
        miniAppId: String?,
        listener: LoaderListener2,
    ) {
        listener.showLoader()
        viewModelScope.launch(setErrorHandler(null)) {
            withContext(Dispatchers.Main) {
                val response: Response<BaseRes> = if (type == "Fev")
                    MyApi().miniAppFev(userId, miniAppId)
                else
                    MyApi().miniAppUnFev(userId, miniAppId)
                listener.dismissLoader()
                if (response.body()?.errorCode == "0") {
                    listener.onSuccess(pos, null, "", type)
                } else {
                    listener.showMess(response.body()?.errorMsg)
                }
            }
        }
    }

}