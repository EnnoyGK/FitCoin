package com.example.pedometerappkt.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.pedometerappkt.R
import com.example.pedometerappkt.other.CustomMarkerView
import com.example.pedometerappkt.other.TrackingUtility
import com.example.pedometerappkt.ui.viewmodels.MainViewModel
import com.example.pedometerappkt.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.barChart
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalCalories
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalCoins
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalDistance
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalSteps
import kotlinx.android.synthetic.main.statistics.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment:Fragment(R.layout.statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart(){

        barChart.xAxis.apply{
            position = XAxis.XAxisPosition.BOTTOM

            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply{
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.axisRight.apply{
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.apply{
            description.text = "Steps Over Time"
            legend.isEnabled = false
            isDragEnabled = true

        }
    }

    private fun subscribeToObservers(){
        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let{
                val km = it / 1000
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "Расстояние\n${totalDistance}km"
                tvTotalDistance.text = totalDistanceString
            }
        })
        viewModel.totalSteps.observe(viewLifecycleOwner, Observer {
            it?.let{
                val steps = it
                val totalStepsString = "Всего шагов\n${steps}"
                tvTotalSteps.text = totalStepsString
            }
        })
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
            it?.let{
                val calories = it
                val totalCaloriesString = "Всего калорий\n${calories}"
                tvTotalCalories.text = totalCaloriesString
            }
        })
        viewModel.totalCoinsEarned.observe(viewLifecycleOwner, Observer {
            it?.let{
                val coins = it
                val totalCoinsString = "Всего монет\n${coins}"
                tvTotalCoins.text = totalCoinsString
            }
        })
        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
            it?.let{
                val allSteps = it.indices.map { i -> BarEntry(i.toFloat() + 50f, it[i].steps.toFloat())}
                val barDataSet = BarDataSet(allSteps, "Steps Over Time").apply{
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                barChart.data = BarData(barDataSet)
                barChart.setVisibleXRangeMaximum(5f)
                barChart.moveViewToX(5f)
                barChart.setFitBars(false)
                barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                barChart.invalidate()
            }
        })
    }
}