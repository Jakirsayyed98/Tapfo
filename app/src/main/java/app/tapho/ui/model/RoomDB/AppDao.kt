package app.tapho.ui.model.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.tapho.ui.model.HomeRes

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(HomeResRoomDB: HomeRes)


    @Query("SELECT * FROM HomeRes")
    fun getAllDataSet():LiveData<List<HomeRes>>

    @Update
    fun UpdateItems(HomeResRoomDB: HomeRes)


}