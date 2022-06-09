package com.example.pedometerappkt.other

import android.content.Context
import com.example.pedometerappkt.db.Session
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val sessions: List<Session>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height / 2f)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e == null){
            return
        }
        val currentSessionId = e.x.toInt()
        val session = sessions[currentSessionId]

        val calendar = Calendar. getInstance().apply{
            timeInMillis = session.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tvDate.text = dateFormat.format(calendar.time)

        val distanceInKm = "${session.distanceInMeters / 1000f}km"
        tvDistance.text = distanceInKm

        val steps = "${session.steps}"
        tvSteps.text = steps

        val coins = "${session.coinsEarned}"
        tvCoins.text = coins


        val caloriesBurned = "${session.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }
}