package com.example.oriencoop_score.di

import com.example.oriencoop_score.api.ClienteService
import com.example.oriencoop_score.api.FacturasService
import com.example.oriencoop_score.api.LoginService
import com.example.oriencoop_score.api.MindicatorInterface
import com.example.oriencoop_score.api.MisProductosService
import com.example.oriencoop_score.api.MovimientosService
import com.example.oriencoop_score.api.SucursalesService
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

    object ApiConfig {
        const val BASE_URL = "http://192.168.120.8:8001/v1/"
    }


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
    @Named("api-parametro")
    fun provideRetrofitParametro(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5000/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    @Named("api-csocial")
    fun provideRetrofitCsocial(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5003/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("api-dap")
    fun provideRetrofitDap(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5004/v1/")
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

    @Provides
    @Singleton
    @Named("api-lcc")
    fun provideRetrofitLcc(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5007/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("api-lcr")
    fun provideRetrofitLcr(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.120.8:5008/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

/* ***** API-GATEWAY *****
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
*/

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

    /********************************* SERVICIOS RETROFIT *************************************/

    @Provides
    @Singleton
    fun mindicatorsService(@Named("mindicators") retrofit: Retrofit): MindicatorInterface {
        return retrofit.create(MindicatorInterface::class.java)
    }

    /****** SERVICIOS API-CLIENTE ******/
    @Provides
    @Singleton
    fun provideLoginService(@Named("api-cliente") retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideClienteInfoService(@Named("api-cliente") retrofit: Retrofit): ClienteService {
        return retrofit.create(ClienteService::class.java)
    }

    /****** SERVICIOS API-PARAMETRO ******/
    @Provides
    @Singleton
    fun provideFacturasService(@Named("api-parametro") retrofit: Retrofit): FacturasService {
        return retrofit.create(FacturasService::class.java)
    }

    @Provides
    @Singleton
    fun provideSucursalesService(@Named("api-parametro") retrofit: Retrofit): SucursalesService {
        return retrofit.create(SucursalesService::class.java)
    }

    /****** SERVICIOS API-CREDITO ******/
    @Provides
    @Singleton
    @MisProductosCredito
    fun provideMisProductosService(@Named("api-credito") retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientosService(@Named("api-credito") retrofit: Retrofit): MovimientosService {
        return retrofit.create(MovimientosService::class.java)
    }

    /****** SERVICIOS API-AHORRO ******/
    @Provides
    @Singleton
    @MisProductosAhorro
    fun provideMisProductosServiceAhorro(@Named("api-ahorro") retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }

    @Provides
    @Singleton
    @MisMovimientosAhorro
    fun provideMovimientosServiceAhorro(@Named("api-ahorro") retrofit: Retrofit): MovimientosService {
        return retrofit.create(MovimientosService::class.java)
    }

    /****** SERVICIOS API-LCC ******/
    @Provides
    @Singleton
    @MisProductosLcc
    fun provideMisProductosServiceLcc(@Named("api-lcc") retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }

    /****** SERVICIOS API-LCR ******/
    @Provides
    @Singleton
    @MisProductosLcr
    fun provideMisProductosServiceLcr(@Named("api-lcr")retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }

    @Provides
    @Singleton
    @MisMovimientosLcr
    fun provideMovimientosServiceLcr(@Named("api-lcr") retrofit: Retrofit): MovimientosService {
        return retrofit.create(MovimientosService::class.java)
    }


    /****** SERVICIOS API-CSOCIAL ******/
    @Provides
    @Singleton
    @MisProductosCsocial
    fun provideMisProductosServiceCsocial(@Named("api-csocial")retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }

    @Provides
    @Singleton
    @MisMovimientosCsocial
    fun provideMovimientosServiceCsocial(@Named("api-csocial") retrofit: Retrofit): MovimientosService {
        return retrofit.create(MovimientosService::class.java)
    }

    /****** SERVICIOS API-DAP ******/
    @Provides
    @Singleton
    @MisProductosDap
    fun provideMisProductosServiceDap(@Named("api-dap")retrofit: Retrofit): MisProductosService {
        return retrofit.create(MisProductosService::class.java)
    }

}