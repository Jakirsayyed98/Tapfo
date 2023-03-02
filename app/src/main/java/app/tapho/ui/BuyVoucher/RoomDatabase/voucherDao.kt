package app.tapho.ui.BuyVoucher.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface voucherDao {

    @Insert
    suspend fun insert(vouchersData: VouchersData)


//    @Update
    @Query("UPDATE VoucherDataTable SET Qty=:Qty,PaybleAmount=:PaybleAmount WHERE denominationKey LIKE :denominationKey")
    suspend fun update(Qty: String, PaybleAmount: String, denominationKey: String)

    @Delete
    suspend fun delete(vouchersData: VouchersData)

    @Query("SELECT * from VoucherDataTable")
    fun getAllVouchersData()  :  LiveData<List<VouchersData>>

    @Query("DELETE FROM VoucherDataTable")
    fun deleteAll()


}