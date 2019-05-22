package com.example.mvvm.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Repository::class, PullRequest::class], version = 1, exportSchema = false)
@TypeConverters(UserTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

    abstract fun pullRequestDao(): PullRequestDao
}