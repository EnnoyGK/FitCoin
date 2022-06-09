package com.example.pedometerappkt.repos

import com.example.pedometerappkt.db.Session
import com.example.pedometerappkt.db.SessionDAO
import com.example.pedometerappkt.db.Shop
import com.example.pedometerappkt.db.ShopDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val sessionDao:SessionDAO,
    val shopDao: ShopDAO
){
    suspend fun insertSession(session: Session) = sessionDao.insertSession(session)

    suspend fun deleteSession(session: Session) = sessionDao.deleteSession(session)

    fun getAllSessionsSortedByDate() = sessionDao.getAllSessionsSortedByDate()

    fun getAllSessionsSortedByAvgSpeed() = sessionDao.getAllSessionsSortedByAvgSpeed()

    fun getAllSessionsSortedByDistance() = sessionDao.getAllSessionsSortedByDistance()

    fun getAllSessionsSortedBySteps() = sessionDao.getAllSessionsSortedBySteps()

    fun  getTotalDistance() = sessionDao.getTotalDistance()

    fun  getTotalSteps() = sessionDao.getTotalSteps()

    fun getTotalCaloriesBurned() = sessionDao.getTotalCaloriesBurned()

    fun getTotalCoinsEarned() = sessionDao.getTotalCoinsEarned()

    suspend fun insertShop(shop: Shop) = shopDao.insertShop(shop)

    suspend fun deleteShop(shop: Shop) = shopDao.deleteShop(shop)

    fun getAllFoodShops() = shopDao.getAllFoodShops()

    fun getAllSportShops() = shopDao.getAllSportShops()

    fun getAllBeautyShops() = shopDao.getAllBeautyShops()
}