package com.example.almondinimes.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.almondinimes.AlmondiApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitClient {
    private const val BASE_URL = "https://api.jikan.moe/v4/"

    private val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
    private val cache = Cache(File(AlmondiApplication.getContext().cacheDir, "http-cache"), cacheSize)

    private val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            
            // Si no hay internet, forzamos el uso de la caché (si existe)
            if (!isNetworkAvailable()) {
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7) // 7 días
                    .build()
            }
            
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: JikanApiService by lazy {
        retrofit.create(JikanApiService::class.java)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = AlmondiApplication.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
