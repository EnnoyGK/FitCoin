package com.example.pedometerappkt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pedometerappkt.R
import com.example.pedometerappkt.other.Constants.KEY_SHOP_TYPE
import com.example.pedometerappkt.other.ShopType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.catalog_categories_recyclerview_items.*
import javax.inject.Inject

@AndroidEntryPoint
class CatalogFilterFragment: Fragment(R.layout.catalog_categories_recyclerview_items) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodCard.setOnClickListener(){
            sharedPref.edit().putString(KEY_SHOP_TYPE, ShopType.FOOD.toString())
            findNavController().navigate(R.id.action_catalogFilterFragment_to_shopsFoodFragment)
        }

        beautyCard.setOnClickListener(){
            sharedPref.edit().putString(KEY_SHOP_TYPE, ShopType.BEAUTY.toString())
            findNavController().navigate(R.id.action_catalogFilterFragment_to_shopsBeautyFragment)
        }

        sportCard.setOnClickListener(){
            sharedPref.edit().putString(KEY_SHOP_TYPE, ShopType.SPORT.toString())
            findNavController().navigate(R.id.action_catalogFilterFragment_to_shopsFragment)
        }
    }

}