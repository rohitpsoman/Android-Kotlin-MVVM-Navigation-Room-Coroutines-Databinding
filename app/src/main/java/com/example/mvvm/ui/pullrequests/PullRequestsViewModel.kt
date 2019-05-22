package com.example.mvvm.ui.pullrequests

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.model.PullRequest
import com.example.mvvm.model.Response
import com.example.mvvm.model.repository.AppRepository
import com.example.mvvm.utils.SingleEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class PullRequestsViewModel(private val appRepository: AppRepository) : ViewModel() {

    val repositoryNameLiveData = MutableLiveData<String>()
    val pullRequestsLiveData = MutableLiveData<Response<List<PullRequest>>>()
    val clearPullRequestsLiveData = MutableLiveData<SingleEvent<Boolean>>()
    val changeRepositoryLiveData = MutableLiveData<SingleEvent<Response<Boolean>>>()
    val FIRST_PAGE = 1


    init {
        getRepositoryName()
        getPullRequests(FIRST_PAGE)
    }

    private fun getRepositoryName() {
        repositoryNameLiveData.postValue(appRepository.getRepositoryName())
    }

    fun getPullRequests(page: Int) {
        if (page == 1) {
            clearPullRequestsLiveData.postValue(SingleEvent(true))
            pullRequestsLiveData.postValue(Response.Loading(true))
        }
        val handler = CoroutineExceptionHandler { _, throwable ->
            pullRequestsLiveData.postValue(Response.Error(throwable))
        }
        viewModelScope.launch(handler) {
            try {
                pullRequestsLiveData.postValue(Response.Success(appRepository.getPullRequests(page)))
            } catch (exception: Exception) {
                if (page == 1) {
                    pullRequestsLiveData.postValue(Response.Error(exception,appRepository.getPullRequestsFromDB()))
                }else{
                    throw exception
                }
            }
        }
    }

    fun changeRepository() {
        changeRepositoryLiveData.postValue(
            SingleEvent(Response.Loading(true))
        )
        viewModelScope.launch {
            appRepository.changeRepository()
            changeRepositoryLiveData.postValue(SingleEvent(Response.Success(true)))
        }
    }
}