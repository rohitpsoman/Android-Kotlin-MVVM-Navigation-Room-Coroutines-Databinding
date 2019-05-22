package com.example.mvvm.model.repository

import com.example.mvvm.model.AppDatabase
import com.example.mvvm.model.PullRequest
import com.example.mvvm.model.Repository
import com.example.mvvm.network.NetworkService
import com.example.mvvm.utils.Prefs
import kotlinx.coroutines.delay

class AppRepository(
    private val prefs: Prefs, private val networkService: NetworkService, private val appDatabase: AppDatabase
) {

    // Items per page for query
    private val ITEMS_PER_PAGE = 20
    // Either open, closed, or all to filter by state.
    private val REQUEST_STATE = "all"

    suspend fun getRepoSelectedStatus(): Boolean {
        delay(2000)
        return prefs.getRepoSelectedStatus()
    }

    suspend fun getRepo(ownerName: String, repositoryName: String) =
        networkService.getRepo(ownerName, repositoryName).await()

    suspend fun getRepoFromDB() = appDatabase.repositoryDao().getRepository()

    suspend fun saveRepo(repository: Repository) {
        appDatabase.repositoryDao().apply {
            nukeTable()
            insert(repository)
        }
        prefs.setRepoSelected(true)
        prefs.setRepoName("${repository.ownerName}/${repository.name}")
    }

    suspend fun getPullRequests(page: Int): List<PullRequest> {
        val repo = getRepoFromDB()
        val pullRequests = networkService.getPullRequests(
            repo.ownerName,
            repo.name,
            page,
            ITEMS_PER_PAGE,
            REQUEST_STATE
        ).await()
        if (page == 1) {
            appDatabase.pullRequestDao().apply {
                nukeTable()
                insert(pullRequests)
            }
        }
        return pullRequests
    }

    suspend fun getPullRequestsFromDB() = appDatabase.pullRequestDao().getPullRequests().reversed()

    fun getRepositoryName() = prefs.getRepoName()

    suspend fun changeRepository() {
        appDatabase.pullRequestDao().nukeTable()
        appDatabase.repositoryDao().nukeTable()
        prefs.setRepoSelected(false)
    }
}