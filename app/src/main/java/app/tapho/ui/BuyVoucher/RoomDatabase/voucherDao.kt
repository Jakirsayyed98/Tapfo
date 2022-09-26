package app.tapho.ui.BuyVoucher.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface voucherDao {

    @Insert
    suspend fun insert(vouchersData: VouchersData)


//    @Update
//    @Query("UPDATE VoucherDataTable SET Qty=:Qty WHERE denominationId LIKE :Denominationid")
    suspend fun update(Qty : String ,Denominationid : String,)

    @Delete
    suspend fun delete(vouchersData: VouchersData)

    @Query("SELECT * from VoucherDataTable")
    fun getAllVouchersData()  :  LiveData<List<VouchersData>>


}