package com.hotsquad.hotsquadlist.koin.module

import com.hotsquad.hotsquadlist.BuildConfig
import com.hotsquad.hotsquadlist.network.RemoteConstant
import com.hotsquad.hotsquadlist.network.SoService
import com.hotsquad.hotsquadlist.storage.AppPreferences
import com.hotsquad.hotsquadlist.utils.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val NetworkModule = module {

    /**
     * Define a Singleton of Retrofit
     * Get Single instance of Retrofit
     */
    single {
        getRetrofitInstance(get(), get())
    }

    /**
     * Define a Singleton of OkHttpClient
     * Get Single instance of OkHttpClient
     */
    single {
        provideOkHttpClient()
    }

    /**
     * Define a Singleton of SoService [Api Service]
     * Get Single instance of SoService [Api Service]
     */
    single {
        provideAPIClient(get())
    }

    /**
     * Define a Singleton of GsonConverterFactory
     * Get Single instance of GsonConverterFactory
     */
    single {
        getGsonConverterFactory()
    }
}

fun getRetrofitInstance(gsonConverterFactory: GsonConverterFactory, client: OkHttpClient): Retrofit
{
    return Retrofit.Builder()
        .baseUrl(RemoteConstant.BASE_URL)
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

fun provideOkHttpClient(): OkHttpClient{

    val client = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        client.addInterceptor(LoggingInterceptor())
    }

    client.addInterceptor { chain: Interceptor.Chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ".plus(AppPreferences.loginData?.token))
            .build()
        chain.proceed(newRequest)
    }

    return client.build()
}

fun provideAPIClient(retrofit: Retrofit): SoService {
    return retrofit.create(SoService::class.java)
}

fun getGsonConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}