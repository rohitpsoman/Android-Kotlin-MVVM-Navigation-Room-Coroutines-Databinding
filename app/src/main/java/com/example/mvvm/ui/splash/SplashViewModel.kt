package com.example.mvvm.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.model.repository.AppRepository
import com.example.mvvm.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(private val appRepository: AppRepository) : ViewModel() {

    val repoSelectedLiveData = MutableLiveData<SingleEvent<Boolean>>()


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val status = appRepository.getRepoSelectedStatus()
                repoSelectedLiveData.postValue(SingleEvent(status))
            }
        }
    }
}