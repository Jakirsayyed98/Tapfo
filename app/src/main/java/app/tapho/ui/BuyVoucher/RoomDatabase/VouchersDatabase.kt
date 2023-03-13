package app.tapho.ui.BuyVoucher.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities =[VouchersData::class], version = 1)
abstract class VouchersDatabase : RoomDatabase(){

    abstract fun getVouchersDatabase() : voucherDao

    companion object{
        @Volatile
        private var INSTANCE: VouchersDatabase? =null

        fun getDatabase(context: Context): VouchersDatabase{
//            if (INSTANCE == null){
//                synchronized(this){
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,VouchersDatabase::class.java,"vouchersdata_db").build()
//                }
//
//            }
            return INSTANCE!!
        }
    }


}