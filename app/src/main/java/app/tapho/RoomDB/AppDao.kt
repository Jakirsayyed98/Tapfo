package app.tapho.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.tapho.ui.model.HomeRes
import app.tapho.ui.scanner.model.Data

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(HomeResRoomDB: HomeRes)


    @Query("SELECT * FROM HomeRes")
    fun getAllDataSet():LiveData<List<HomeRes>>

    @Update
    fun UpdateItems(HomeResRoomDB: HomeRes)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun AddPRoductToCart(ProductDatas:Data)

    @Query("SELECT * FROM Data")
    fun getAllProductSet():LiveData<List<Data>>


    @Query("SELECT * FROM Data WHERE barcode=:barcode")
    fun getProductByBarcode(barcode: String):Data



    @Query("SELECT * FROM Data WHERE barcode=:barcode")
    fun ProductByBarcodeISExist(barcode: String):Boolean
    //    fun getProductByBarcode(QrCode: String):LiveData<ProductData>

    @Query("UPDATE Data set buyingQty= :buyingQty  WHERE barcode = :barcode")
    fun UpdateProductToCart(buyingQty:Int,barcode:String)


    @Query("Delete FROM Data")
    fun DeleteAllProduct();


}