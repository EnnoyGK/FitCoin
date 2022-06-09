package com.example.pedometerappkt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pedometerappkt.R
import com.example.pedometerappkt.db.Session
import com.example.pedometerappkt.db.Shop
import com.example.pedometerappkt.other.TrackingUtility
import kotlinx.android.synthetic.main.catalog_recyclerview_items.view.*
import kotlinx.android.synthetic.main.item_session.view.*
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class ShopsAdapter : RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return  oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Shop>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        return ShopViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.catalog_recyclerview_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = differ.currentList[position]
        holder.itemView.apply{
           // Glide.with(this).load(shop.icon).into(ivShopIcon)

            val name = "${shop.name}"
            tvShopName.text = name

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
