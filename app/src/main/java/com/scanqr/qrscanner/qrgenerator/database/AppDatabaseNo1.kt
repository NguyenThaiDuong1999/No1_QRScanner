package com.scanqr.qrscanner.qrgenerator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryModel::class], version = 1, exportSchema = false)
abstract class AppDatabaseNo1 : RoomDatabase() {

    companion object {
        private const val DB_NAME = "scan_qr.db"

        @Volatile
        private var instance: AppDatabaseNo1? = null

        fun getInstance(context: Context): AppDatabaseNo1 {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabaseNo1 {
            return Room.databaseBuilder(context.applicationContext, AppDatabaseNo1::class.java, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun appDao(): AppDaoNo1

}