package com.example.pedometerappkt.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pedometerappkt.other.ShopType

@Entity(tableName = "shop_table")
data class Shop(
    //var icon: Bitmap? = null,
    //var image: Bitmap? = null,
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    var type: ShopType = ShopType.FOOD,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
