package app.tapho.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.tapho.ui.model.HomeRes
import app.tapho.ui.scanner.model.Data


@Database(entities = [HomeRes::class,Data::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase :RoomDatabase() {
    abstract fun appDao() : AppDao
}

@Volatile
private lateinit var INSTANCE : AppDatabase

fun getDatabase(context:Context):AppDatabase{
    synchronized(AppDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "database").build()
        }
    }

    return INSTANCE

}