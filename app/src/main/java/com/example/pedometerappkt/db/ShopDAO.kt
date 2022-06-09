package com.example.pedometerappkt.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)

    @Query("SELECT * FROM shop_table WHERE type='Food'")
    fun getAllFoodShops(): LiveData<List<Shop>>

    @Query("SELECT * FROM shop_table WHERE type='Sport'")
    fun getAllSportShops(): LiveData<List<Shop>>

    @Query("SELECT * FROM shop_table WHERE type='Beauty'")
    fun getAllBeautyShops(): LiveData<List<Shop>>
}