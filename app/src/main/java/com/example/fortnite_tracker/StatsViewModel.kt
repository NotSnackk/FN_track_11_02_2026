package com.example.fortnite_tracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortnite_tracker.api.ApiClient
import com.example.fortnite_tracker.models.BrStatsData
import kotlinx.coroutines.launch

class StatsViewModel : ViewModel() {

    private val _playerStatsLiveData = MutableLiveData<BrStatsData?>()
    val playerStatsLiveData: LiveData<BrStatsData?> = _playerStatsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun loadStats(nickname: String) {
        viewModelScope.launch {
            try {
                val statsResponse = ApiClient.retrofitService.getBrStats(nickname, accountType = "epic")
                if (statsResponse.isSuccessful) {
                    _playerStatsLiveData.postValue(statsResponse.body()?.data)
                } else {
                    _errorLiveData.postValue("Blad komunikacji z API.")
                }
            } catch (e: Exception) {
                _errorLiveData.postValue("Blad komunikacji z API: ${e.message}")
            }
        }
    }
}
