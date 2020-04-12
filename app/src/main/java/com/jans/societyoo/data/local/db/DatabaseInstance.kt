package com.jans.societyoo.data.local.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.services.MicroService
import com.jans.societyoo.model.services.Provider
import com.jans.societyoo.model.services.Service

@Database(entities = [FlatDetail::class, UserDetail::class, Service::class, MicroService::class, Provider::class], version = 2, exportSchema = false)
abstract class DatabaseInstance : RoomDatabase() {
    abstract val databaseDAO: DatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseInstance? = null
        val DB_NAME = "app_data_database"
        fun getInstance(context: Context): DatabaseInstance {
            synchronized(this){
                var instance = INSTANCE
                if(instance==null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseInstance::class.java,
                            DB_NAME
                        ).fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }

        }


    }
}