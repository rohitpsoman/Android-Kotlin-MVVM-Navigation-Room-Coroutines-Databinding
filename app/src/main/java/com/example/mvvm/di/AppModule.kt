package com.example.mvvm.di

import androidx.room.Room
import com.example.mvvm.model.AppDatabase
import com.example.mvvm.model.repository.AppRepository
import com.example.mvvm.network.HeaderInterceptor
import com.example.mvvm.network.NetworkService
import com.example.mvvm.ui.pullrequests.PullRequestsViewModel
import com.example.mvvm.ui.repo.RepoViewModel
import com.example.mvvm.ui.splash.SplashViewModel
import com.example.mvvm.utils.Constants
import com.example.mvvm.utils.Prefs
import com.example.mvvm.utils.SharedPreferencesHelper
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single { SharedPreferencesHelper(get()) }
    single { Prefs(get()) }
    single { AppRepository(get(), get(), get()) }

    single { GsonBuilder().create() }
    single { GsonConverterFactory.create(get()) }
    single { CoroutineCallAdapterFactory() }
    single { HeaderInterceptor() }
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single {
        OkHttpClient().newBuilder().apply {
            addInterceptor(get<HeaderInterceptor>())
            addInterceptor(get<HttpLoggingInterceptor>())
            connectTimeout(0, TimeUnit.SECONDS)
            readTimeout(0, TimeUnit.SECONDS)
            writeTimeout(0, TimeUnit.SECONDS)
        }.build()
    }
    single {
        Retrofit.Builder().apply {
            client(get())
            baseUrl(Constants.BASE_URL)
            addConverterFactory(get<GsonConverterFactory>())
            addCallAdapterFactory(get<CoroutineCallAdapterFactory>())
        }.build()
    }
    single { get<Retrofit>().create(NetworkService::class.java) }
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "github_db").build() }

    viewModel { SplashViewModel(get()) }
    viewModel { RepoViewModel(get()) }
    viewModel { PullRequestsViewModel(get()) }
}