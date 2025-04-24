package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.FacturasLcc
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
    @Named("mindicators")
    fun provideRetrofitMindicators(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://172.20.0.57:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("api-parametro")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            //https://apiservice.oriencoop.cl/parametro/v1/
            .baseUrl("http://192.168.120.8:5000/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("api-cliente")
    fun provideRetrofitCliente(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5001/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("api-credito")
    fun provideRetrofitCredito(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5005/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("api-ahorro")
    fun provideRetrofitAhorro(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5006/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }




    /********************************* SERVICIOS RETROFIT *************************************/

    @Provides
    @Singleton
    fun mindicatorsService(@Named("mindicators") retrofit: Retrofit): MindicatorInterface { // Function to provide MindicatorInterface
        return retrofit.create(MindicatorInterface::class.java)
    }

    /****** SERVICIOS API-CLIENTE ******/
    @Provides
    @Singleton
    fun provideLoginService(@Named("api-cliente") retrofit: Retrofit): LoginService { // Function to provide LoginService, injects Retrofit
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideClienteInfoService(@Named("api-cliente")retrofit: Retrofit): ClienteService { // Function to provide MovimientosLccService, injects Retrofit
        return retrofit.create(ClienteService::class.java)
    }
    /****** SERVICIOS API-PARAMETRO ******/

    @Provides
    @Singleton
    fun provideFacturasService(@Named("api-parametro")retrofit: Retrofit): FacturasService { // Function to provide FacturasService, injects Retrofit
        return retrofit.create(FacturasService::class.java)
    }

    @Provides
    @Singleton
    fun provideSucursalesService(@Named("api-parametro")retrofit: Retrofit): SucursalesService { // Function to provide MovimientosLccService, injects Retrofit
        return retrofit.create(SucursalesService::class.java)
    }

    /****** SERVICIOS API-CREDITO ******/
    @Provides
    @Singleton
    @MisProductosCredito
    fun provideMisProductosService(@Named("api-credito")retrofit: Retrofit): MisProductosService { // Function to provide MisProductosService, injects Retrofit
        return retrofit.create(MisProductosService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosService(@Named("api-credito")retrofit: Retrofit): MovimientosService { // Function to provide MovimientosService, injects Retrofit
        return retrofit.create(MovimientosService::class.java)
    }

    /****** SERVICIOS API-CREDITO ******/
    @Provides
    @Singleton
    @MisProductosAhorro
    fun provideMisProductosServiceAhorro(@Named("api-ahorro") retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }








}
