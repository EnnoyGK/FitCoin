package com.example.pedometerappkt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Session::class, Shop::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class SessionDatabase : RoomDatabase() {

    abstract fun getSessionDao(): SessionDAO
    abstract fun getShopDao(): ShopDAO
}