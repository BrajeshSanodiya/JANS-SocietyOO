package com.jans.societyoo.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail

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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userDetail: UserDetail)

    @Query("SELECT * FROM user_data_table")
    fun getUserDetail() : UserDetail

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAllUsers()
}