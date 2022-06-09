package com.example.pedometerappkt.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
// Probably will change to Day class
data class Session(
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var steps: Int = 0,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0,
    var coinsEarned: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}