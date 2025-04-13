package com.wojciechosak.openmeteo.di

import com.wojciechosak.openmeteo.di.ApiConfig.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private object ApiConfig {
    const val BASE_URL = "https://api.open-meteo.com/v1/"
    const val FORECAST_ENDPOINT = "forecast?"
    const val CURRENT_ENDPOINT = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,is_day"
    const val FORECAST_DAYS = "forecast_days"
}

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}