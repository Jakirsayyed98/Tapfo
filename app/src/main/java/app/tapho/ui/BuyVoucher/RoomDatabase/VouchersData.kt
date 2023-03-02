package app.tapho.ui.BuyVoucher.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "VoucherDataTable")
data class VouchersData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val MiniApp_icon: String,
    val name: String,
    val ActualAmount: String,
    val PaybleAmount: String,
    val Qty: String,
    val voucherID: String,
    val denominationKey: String,
    val denominationId: String
)