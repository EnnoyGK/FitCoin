package com.example.pedometerappkt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedometerappkt.db.Shop
import com.example.pedometerappkt.other.ShopType

import com.example.pedometerappkt.other.SortType
import com.example.pedometerappkt.repos.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val foodShops = mainRepository.getAllFoodShops()
    val beautyShops = mainRepository.getAllBeautyShops()
    val sportShops = mainRepository.getAllSportShops()

    val shops = MediatorLiveData<List<Shop>>()

    var shopType = ShopType.SPORT


    init{
        shops.addSource(foodShops){ result ->
            if(shopType == ShopType.FOOD){
                result?.let{ shops.value = it }
            }
        }
    }

   init{
        shops.addSource(beautyShops){ result ->
            if(shopType == ShopType.BEAUTY){
                result?.let{ shops.value = it }
            }
        }
    }

    init{
        shops.addSource(sportShops){ result ->
            if(shopType == ShopType.SPORT){
                result?.let{ shops.value = it }
            }
        }
    }

    fun filterShops(type: ShopType){
        when(type){
            ShopType.FOOD -> foodShops.value?.let {shops.value = it}
            ShopType.BEAUTY -> beautyShops.value?.let {shops.value = it}
            ShopType.SPORT -> sportShops.value?.let {shops.value = it}
        }.also {
            shopType = type
        }
    }

    fun insertShop(shop: Shop) = viewModelScope.launch {
        mainRepository.insertShop(shop)
        Timber.d("Shop Inserted")
    }
}