package com.example.mvvm.ui.repo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.R
import com.example.mvvm.model.RepoDetails
import com.example.mvvm.model.Repository
import com.example.mvvm.model.Response
import com.example.mvvm.model.repository.AppRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RepoViewModel(private val appRepository: AppRepository) : ViewModel() {

    val repoDetails = RepoDetails()

    val ownerNameErrorLiveData = MutableLiveData<Int>()
    val repoNameErrorLiveData = MutableLiveData<Int>()
    val responseLiveData = MutableLiveData<Response<Repository>>()

    fun getRepository() {
        when {
            repoDetails.ownerName.isEmpty() -> ownerNameErrorLiveData.postValue(R.string.repo_owner_error)
            repoDetails.repoName.isEmpty() -> repoNameErrorLiveData.postValue(R.string.repo_name_error)
            else -> {
                val handler = CoroutineExceptionHandler { _, throwable ->
                    responseLiveData.postValue(Response.Error(throwable))
                }
                responseLiveData.postValue(Response.Loading(true))
                viewModelScope.launch(handler) {
                    val repository = appRepository.getRepo(repoDetails.ownerName, repoDetails.repoName).apply {
                        ownerName = repoDetails.ownerName
                    }
                    appRepository.saveRepo(repository)
                    responseLiveData.postValue(Response.Success(repository))
                }
            }
        }
    }
}