package com.scanqr.qrscanner.qrgenerator.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryModel(model: HistoryModel)

    @Query("select * from HistoryModel where historyType = :type")
    fun getAllHistoryByType(type: HistoryType): MutableList<HistoryModel>

    @Query("select * from HistoryModel where id = :id")
    fun getHistory(id: Int): HistoryModel

    @Query("delete from HistoryModel where id = :id")
    suspend fun deleteHistory(id: Int)

    @Query("select count(*) from HistoryModel where historyType = :type")
    fun getCountSizeByType(type: HistoryType): Int

}