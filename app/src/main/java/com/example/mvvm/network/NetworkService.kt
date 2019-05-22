package com.example.mvvm.network

import com.example.mvvm.model.PullRequest
import com.example.mvvm.model.Repository
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("{owner_name}/{repo_name}")
    fun getRepo(
        @Path("owner_name") ownerName: String,
        @Path("repo_name") repositoryName: String
    ): Deferred<Repository>

    @GET("{owner_name}/{repo_name}/pulls")
    fun getPullRequests(
        @Path("owner_name") ownerName: String,
        @Path("repo_name") repositoryName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("state") state: String
    ): Deferred<List<PullRequest>>
}