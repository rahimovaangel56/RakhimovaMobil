package com.example.rakhimovakp.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.data.model.CartItem
import com.example.rakhimovakp.data.local.dao.CarDao
import com.example.rakhimovakp.data.local.dao.CartDao
import com.example.rakhimovakp.data.local.dao.UserDao
import com.example.rakhimovakp.data.model.User

@Database(
    entities = [Car::class, CartItem::class, User::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rakhimova_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}