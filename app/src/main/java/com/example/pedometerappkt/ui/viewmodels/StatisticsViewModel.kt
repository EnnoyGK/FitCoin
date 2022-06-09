package com.example.pedometerappkt.ui.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.pedometerappkt.repos.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val totalDistance = mainRepository.getTotalDistance()
    val totalSteps = mainRepository.getTotalSteps()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalCoinsEarned = mainRepository.getTotalCoinsEarned()

    val runsSortedByDate = mainRepository.getAllSessionsSortedByDate()
}