package com.example.dbrueckerstattung.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dbrueckerstattung.entity.Customer

// Database für SQLite, implementiert als Singleton
@Database(entities = [Customer :: class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao() : CustomerDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }

}
