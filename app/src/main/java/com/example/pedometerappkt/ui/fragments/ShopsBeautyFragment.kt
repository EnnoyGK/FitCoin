package com.example.pedometerappkt.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pedometerappkt.ui.viewmodels.ShopViewModel
import com.example.pedometerappkt.R
import com.example.pedometerappkt.adapters.ShopsAdapter
import com.example.pedometerappkt.db.Shop
import com.example.pedometerappkt.other.ShopType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.catalog_beauty.*
import kotlinx.android.synthetic.main.catalog_food.*
import kotlinx.android.synthetic.main.catalog_sport.*
import kotlinx.android.synthetic.main.fragment_session.*
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ShopsBeautyFragment: Fragment(R.layout.catalog_beauty) {

   @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: ShopViewModel by viewModels()

   // private lateinit var shopsAdapter: ShopsAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("Creating Shop")

        val shop = Shop( "SportMaster", "description", 10, ShopType.SPORT)
        viewModel.insertShop(shop)

        Timber.d("${viewModel.sportShops.value}")

        //setupRecyclerView()


       when{
            //sharedPreferences.getString(KEY_SHOP_TYPE, "") == ShopType.FOOD.toString() -> viewModel.filterShops(ShopType.FOOD)
            //sharedPreferences.getString(KEY_SHOP_TYPE, "") == ShopType.BEAUTY.toString() -> viewModel.filterShops(ShopType.BEAUTY)
           // sharedPreferences.getString(KEY_SHOP_TYPE, "") == ShopType.SPORT.toString() -> viewModel.filterShops(ShopType.SPORT)

        }
        //viewModel.filterShops(ShopType.SPORT)
        viewModel.shops.observe(viewLifecycleOwner, Observer {
           // shopsAdapter.submitList(it)
        })


        cv_bilita.setOnClickListener(){
            findNavController().navigate(R.id.action_shopsBeautyFragment_to_bilitaFragment)
        }




    }

    /*private fun setupRecyclerView() = rvShops.apply{
        shopsAdapter = ShopsAdapter()
        adapter = shopsAdapter
        layoutManager = LinearLayoutManager(requireContext())
        Timber.d("RV set up")
    }*/



}