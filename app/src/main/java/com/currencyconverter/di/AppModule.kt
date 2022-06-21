package com.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.currencyconverter.BuildConfig
import com.currencyconverter.api.ApiHelper
import com.currencyconverter.api.ApiHelperImpl
import com.currencyconverter.api.ApiService
import com.currencyconverter.db.CurrencyDatabase
import com.currencyconverter.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Singleton
    @Provides
    fun provideCurrencyDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        CurrencyDatabase::class.java,
        Constants.CURRENCY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesCurrencyDao(currencyDatabase: CurrencyDatabase) = currencyDatabase.getCurrencyDao()

}