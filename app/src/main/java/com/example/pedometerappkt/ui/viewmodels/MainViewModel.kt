package com.example.pedometerappkt.ui.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedometerappkt.db.Session
import com.example.pedometerappkt.other.SortType
import com.example.pedometerappkt.repos.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val sessionsSortedByDate = mainRepository.getAllSessionsSortedByDate()
    val sessionsSortedByDistance = mainRepository.getAllSessionsSortedByDistance()
    val sessionsSortedBySteps = mainRepository.getAllSessionsSortedBySteps()
    val sessionsSortedByAvgSpeed = mainRepository.getAllSessionsSortedByAvgSpeed()

    val sessions = MediatorLiveData<List<Session>>()

    var sortType = SortType.DATE

    init{
        sessions.addSource(sessionsSortedByDate){ result ->
            if(sortType == SortType.DATE){
             result?.let{ sessions.value = it }
            }
        }
    }

    init{
        sessions.addSource(sessionsSortedByDistance){ result ->
            if(sortType == SortType.DISTANCE){
                result?.let{ sessions.value = it }
            }
        }
    }

    init{
        sessions.addSource(sessionsSortedBySteps){ result ->
            if(sortType == SortType.STEPS){
                result?.let{ sessions.value = it }
            }
        }
    }

    init{
        sessions.addSource(sessionsSortedByAvgSpeed){ result ->
            if(sortType == SortType.AVG_SPEED){
                result?.let{ sessions.value = it }
            }
        }
    }

    fun sortSessions(sortType: SortType) = when(sortType){
        SortType.DATE -> sessionsSortedByDate.value?.let {sessions.value = it}
        SortType.DISTANCE -> sessionsSortedByDistance.value?.let {sessions.value = it}
        SortType.STEPS -> sessionsSortedBySteps.value?.let {sessions.value = it}
        SortType.AVG_SPEED -> sessionsSortedByAvgSpeed.value?.let {sessions.value = it}
    }.also{
        this.sortType = sortType
    }

    fun insertSession(session: Session) = viewModelScope.launch {
        mainRepository.insertSession(session)
    }
}