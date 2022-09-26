package app.tapho.RechargeApiNetwork

import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RechargeApiInterface {
    @FormUrlEncoded
    @POST("CyrusPlanFatchAPI.aspx?APIID=AP109198&PASSWORD=Jf67c7b8Hf6g")
    suspend fun AllRechargePlans(
        @Field("Operator_Code") Operator_Code: String?,
        @Field("Circle_Code") Circle_Code: String?,
        @Field("MobileNumber") MobileNumber: String?,
        @Field("PageID") PageID: String?,
        @Field("FORMAT") FORMAT: String?,

        ): Response<getRechargePlans>
}