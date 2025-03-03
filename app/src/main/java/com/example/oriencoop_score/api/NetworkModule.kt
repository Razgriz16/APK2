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
    fun provideCuentaCapService(@Named("MainApi")retrofit: Retrofit): CuentaCapService { // Function to provide CuentaCapService, injects Retrofit
        return retrofit.create(CuentaCapService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosService(@Named("MainApi")retrofit: Retrofit): MovimientosService { // Function to provide MovimientosService, injects Retrofit
        return retrofit.create(MovimientosService::class.java)
    }

    @Provides
    @Singleton
    fun provideCuentaAhorroService(@Named("MainApi")retrofit: Retrofit): CuentaAhorroService { // Function to provide CuentaAhorroService, injects Retrofit
        return retrofit.create(CuentaAhorroService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosAhorroService(@Named("MainApi")retrofit: Retrofit): MovimientosAhorroService { // Function to provide MovimientosAhorroService, injects Retrofit
        return retrofit.create(MovimientosAhorroService::class.java)
    }

    @Provides
    @Singleton
    fun provideCreditoCuotasService(@Named("MainApi")retrofit: Retrofit): CreditoCuotasService { // Function to provide CreditoCuotasService, injects Retrofit
        return retrofit.create(CreditoCuotasService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosCreditosService(@Named("MainApi")retrofit: Retrofit): MovimientosCreditos { // Function to provide MovimientosCreditos, injects Retrofit
        return retrofit.create(MovimientosCreditos::class.java)
    }

    @Provides
    @Singleton
    fun provideLccService(@Named("MainApi")retrofit: Retrofit): LccService { // Function to provide LccService, injects Retrofit
        return retrofit.create(LccService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosLccService(@Named("MainApi")retrofit: Retrofit): MovimientosLccService { // Function to provide MovimientosLccService, injects Retrofit
        return retrofit.create(MovimientosLccService::class.java)
    }

    @Provides
    @Singleton
    fun provideLcrService(@Named("MainApi")retrofit: Retrofit): LcrService { // Function to provide LccService, injects Retrofit
        return retrofit.create(LcrService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosLcrService(@Named("MainApi")retrofit: Retrofit): MovimientosLcrService { // Function to provide LccService, injects Retrofit
        return retrofit.create(MovimientosLcrService::class.java)
    }


    @Provides
    @Singleton
    fun provideClienteInfoService(@Named("MainApi")retrofit: Retrofit): ClienteInfoService { // Function to provide MovimientosLccService, injects Retrofit
        return retrofit.create(ClienteInfoService::class.java)
    }

    @Provides
    @Singleton
    fun provideDapService(@Named("MainApi")retrofit: Retrofit): DapService { // Function to provide MovimientosLccService, injects Retrofit
        return retrofit.create(DapService::class.java)
    }


}
