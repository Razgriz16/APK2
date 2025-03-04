package com.example.oriencoop_score.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient { // Function to provide OkHttpClient
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("MainApi")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginService(@Named("MainApi") retrofit: Retrofit): LoginService { // Function to provide LoginService, injects Retrofit
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideMisProductosService(@Named("MainApi")retrofit: Retrofit): MisProductosService { // Function to provide MisProductosService, injects Retrofit
        return retrofit.create(MisProductosService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosService(@Named("MainApi")retrofit: Retrofit): MovimientosService { // Function to provide MovimientosService, injects Retrofit
        return retrofit.create(MovimientosService::class.java)
    }


    @Provides
    @Singleton
    fun provideClienteInfoService(@Named("MainApi")retrofit: Retrofit): ClienteInfoService { // Function to provide MovimientosLccService, injects Retrofit
        return retrofit.create(ClienteInfoService::class.java)
    }

}
