package com.example.pedometerappkt.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SessionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session_table ORDER BY timestamp DESC")
    fun getAllSessionsSortedByDate(): LiveData<List<Session>>

    @Query("SELECT * FROM session_table ORDER BY avgSpeedInKMH DESC")
    fun getAllSessionsSortedByAvgSpeed(): LiveData<List<Session>>

    @Query("SELECT * FROM session_table ORDER BY distanceInMeters DESC")
    fun getAllSessionsSortedByDistance(): LiveData<List<Session>>

    @Query("SELECT * FROM session_table ORDER BY steps DESC")
    fun getAllSessionsSortedBySteps(): LiveData<List<Session>>

    @Query("SELECT SUM(distanceInMeters) FROM session_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT SUM(steps) FROM session_table")
    fun getTotalSteps(): LiveData<Int>

    @Query("SELECT SUM(caloriesBurned) FROM session_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(coinsEarned) FROM session_table")
    fun getTotalCoinsEarned(): LiveData<Int>
}