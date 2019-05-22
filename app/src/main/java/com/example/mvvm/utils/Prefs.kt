package com.example.mvvm.utils

class Prefs(private val sharedPreferencesHelper: SharedPreferencesHelper) {

    fun getRepoSelectedStatus() = sharedPreferencesHelper.getBoolean(Keys.IS_REPO_SELECTED, false)

    fun setRepoSelected(status: Boolean) = sharedPreferencesHelper.putBoolean(Keys.IS_REPO_SELECTED, status)

    fun getRepoName() = sharedPreferencesHelper.getString(Keys.REPO_NAME, "")

    fun setRepoName(repoName: String) = sharedPreferencesHelper.putString(Keys.REPO_NAME, repoName)
}