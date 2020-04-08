package com.jans.societyoo.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.services.MicroService
import com.jans.societyoo.model.services.Provider
import com.jans.societyoo.model.services.Service

@Dao
interface DatabaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlat(flatDetail: FlatDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFlats(flatDetail: List<FlatDetail>)

    @Query("DELETE FROM flat_data_table")
    suspend fun deleteAllFlats()

    @Query("SELECT * FROM flat_data_table")
    suspend fun getAllFlats() : List<FlatDetail>

    @Query("SELECT society_id FROM flat_data_table WHERE user_master_id=:defaultUserId")
    suspend fun getDefaultFlatSocietyId(defaultUserId:Int) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userDetail: UserDetail)

    @Query("SELECT * FROM user_data_table")
    suspend fun getUserDetail() : UserDetail

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAllUsers()





    @Query("DELETE FROM society_service")
    suspend fun deleteAllService()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllService(serviceList: List<Service>)

    @Query("SELECT * FROM society_service")
    suspend fun getAllService():List<Service>

    @Query("DELETE FROM society_micro_service")
    suspend fun deleteAllMicroService()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMicroService(microServiceList: List<MicroService>)

    @Query("SELECT * FROM society_micro_service WHERE serviceId=:serviceID")
    suspend fun getMicroService(serviceID:Int):List<MicroService>

    @Query("DELETE FROM society_service_provider")
    suspend fun deleteAllServiceProvider()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllServiceProvider(providerList: List<Provider>)

    @Query("SELECT * FROM society_service_provider WHERE microServiceId=:microServiceId")
    suspend fun getServiceProvider(microServiceId:Int):List<Provider>

}