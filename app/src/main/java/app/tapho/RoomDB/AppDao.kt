package app.tapho.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.tapho.ui.model.HomeRes
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart

@Dao
interface AppDao {
    // Home Start
    @Insert
    fun insertItems(HomeResRoomDB: HomeRes)

    @Query("SELECT * FROM HomeRes WHERE id=:id")
    fun HomeresByIdISExist(id: String):Boolean


    @Query("SELECT * FROM HomeRes")
    fun getAllDataSet():LiveData<List<HomeRes>>

    @Update
    fun UpdateItems(HomeResRoomDB: HomeRes)

    @Delete
    fun DeleteHomeRes(HomeResRoomDB: HomeRes)
// Home End

// Cart Product Start
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun AddPRoductToCart(ProductDatas:Cart)

    @Query("SELECT * FROM Cart")
    fun getAllProductSet():LiveData<List<Cart>>


    @Query("SELECT * FROM Cart WHERE ean=:barcode")
    fun getProductByBarcode(barcode: String): Cart



    @Query("SELECT * FROM Cart WHERE ean=:barcode")
    fun ProductByBarcodeISExist(barcode: String):Boolean
    //    fun getProductByBarcode(QrCode: String):LiveData<ProductData>

    @Query("UPDATE Cart set qty= :buyingQty,totalPrice=:totalPrice  WHERE ean = :barcode")
    fun UpdateProductToCart(buyingQty:Int,barcode:String,totalPrice:Double)


    @Query("Delete FROM Cart")
    fun DeleteAllProduct();

    @Query("Delete From Cart WHERE ean=:barcode")
    fun DeleteProduct(barcode: String)

    // Cart Product End


    // All Products
    @Insert
    fun insertAllProducts(ProductData: Data)

    @Query("Delete FROM Data")
    fun DeleteAllBusinessProduct();


    @Query("SELECT * FROM Data WHERE ean=:ean")
    fun ProductByEANisExist(ean:String):Boolean
    @Query("SELECT * FROM Data Where ean=:ean")
    fun searchProduct(ean:String): Data


}