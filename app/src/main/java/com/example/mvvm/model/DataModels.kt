package com.example.mvvm.model

import androidx.room.*

data class RepoDetails(
    var ownerName: String = "",
    var repoName: String = ""
)

@Entity(tableName = "repository")
data class Repository(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var private: Boolean = false,
    var ownerName: String = ""
)

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM repository LIMIT 1")
    suspend fun getRepository(): Repository

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repository: Repository)

    @Query("DELETE FROM repository")
    suspend fun nukeTable()
}

@Entity(tableName = "pullRequest")
data class PullRequest(
    @PrimaryKey
    val id: Long = 0,
    val title: String = "",
    val updated_at: String = "",
    val created_at: String = "",
    val state: String = "",
    val number: Long = 0,
    val html_url: String = "",
    @TypeConverters(UserTypeConverter::class)
    val user: User = User()
)

@Dao
interface PullRequestDao {

    @Query("SELECT * FROM pullRequest")
    suspend fun getPullRequests(): List<PullRequest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pullRequestList: List<PullRequest>)

    @Query("DELETE FROM pullRequest")
    suspend fun nukeTable()
}

data class User(
    val login: String = "",
    val avatar_url: String = ""
)