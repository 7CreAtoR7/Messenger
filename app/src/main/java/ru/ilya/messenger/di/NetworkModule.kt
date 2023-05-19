package ru.ilya.messenger.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ilya.messenger.data.network.ApiService
import ru.ilya.messenger.data.network.ApiUrlProvider
import ru.ilya.messenger.data.network.ApiUrlProviderImpl

@Module
class NetworkModule {

    @Provides
    fun provideApiUrlProvider(): ApiUrlProvider {
        return ApiUrlProviderImpl()
    }

    @Provides
    @ApplicationScope
    fun provideApiService(apiUrlProvider: ApiUrlProvider): ApiService {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrlProvider.getApiUrlMethod())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)

    }
}