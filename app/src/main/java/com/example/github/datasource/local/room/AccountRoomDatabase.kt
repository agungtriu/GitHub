package com.example.github.datasource.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.github.datasource.local.room.entity.AccountDetailEntity

@Database(entities = [AccountDetailEntity::class], version = 1)
abstract class AccountRoomDatabase : RoomDatabase() {
    abstract fun AccountDao(): AccountDao

    companion object {
        @Volatile
        private var INSTANCE: AccountRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AccountRoomDatabase {
            if (INSTANCE == null) {
                synchronized(AccountRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AccountRoomDatabase::class.java,
                        "github_database"
                    ).build()
                }
            }
            return INSTANCE as AccountRoomDatabase
        }
    }
}