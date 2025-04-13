package com.wojciechosak.openmeteo.di

import com.wojciechosak.openmeteo.data.WeatherService
import com.wojciechosak.openmeteo.di.ApiConfig.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private object ApiConfig {
    const val BASE_URL = "https://api.open-meteo.com/v1/"
}

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideWeatherService(get()) }
}

private fun provideWeatherService(retrofit: Retrofit): WeatherService {
    return retrofit.create(WeatherService::class.java)
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