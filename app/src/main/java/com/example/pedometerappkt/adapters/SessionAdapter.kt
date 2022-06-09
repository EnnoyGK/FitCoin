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
import com.example.pedometerappkt.other.TrackingUtility
import kotlinx.android.synthetic.main.item_session.view.*
import kotlinx.android.synthetic.main.item_session.view.tvDate
import kotlinx.android.synthetic.main.item_session.view.tvDistance
import kotlinx.android.synthetic.main.item_session.view.tvSteps
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class SessionAdapter : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return  oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Session>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.item_session,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = differ.currentList[position]
        holder.itemView.apply{
            //Glide.with(this).load(session.img).into(ivRunImage)

            val calendar = Calendar. getInstance().apply{
                timeInMillis = session.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed =  "${session.avgSpeedInKMH}km/h"
            tvSteps.text = avgSpeed

            val distanceInKm = "${session.distanceInMeters / 1000f}km"
            tvDistance.text = distanceInKm

            val steps = "${session.steps}"
            tvSteps.text = steps

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(session.timeInMillis)

            val caloriesBurned = "${session.caloriesBurned}kcal"
            tvCaloriesBurned.text = caloriesBurned
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}