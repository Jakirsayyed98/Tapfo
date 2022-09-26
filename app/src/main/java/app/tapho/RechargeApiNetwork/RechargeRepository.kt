package app.tapho.RechargeApiNetwork

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans

class RechargeRepository(private val apiInterFace: RechargeApiInterface) {

    private val RechargePlansLiveData=MutableLiveData<getRechargePlans>()

    val AllPlans:LiveData<getRechargePlans>

    get() = RechargePlansLiveData

    suspend fun getRechargePlans(){
        val result=apiInterFace.AllRechargePlans("1","1","8108893686","0","JSON")
        if (result.body()!=null){
            RechargePlansLiveData.postValue(result.body())
        }
    }

}