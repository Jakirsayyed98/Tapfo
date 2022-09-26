package app.tapho.RechargeApiNetwork

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RechargeViewModel(private val RechargeRepository: RechargeRepository):ViewModel() {

    init {
        viewModelScope.launch (Dispatchers.IO){
            RechargeRepository.getRechargePlans()
        }
    }
        val AllPlansLiveData:LiveData<getRechargePlans>
       get() = RechargeRepository.AllPlans


}