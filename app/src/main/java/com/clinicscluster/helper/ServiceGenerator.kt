package com.clinicscluster.helper

import android.util.Log
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private const val TAG = "@@ServiceGenerator"

    //    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    const val HEADER_CACHE_CONTROL = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"
    var instance: ServiceGenerator? = null
        get() {
            if (field == null) {
                field = ServiceGenerator
            }
            return field
        }
        private set
    private const val cacheSize = (20 * 1024 * 1024).toLong()
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiContants.PREF_base_url)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun retrofitdemo(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache())
            .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
            .addNetworkInterceptor(networkInterceptor()) // only used when network is on
            .addInterceptor(offlineInterceptor())
            .build()
    }

    private fun cache(): Cache {
        return Cache(File(ApplicationInit.getInstance1().cacheDir, "someIdentifier"), cacheSize)
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     * @return
     */
    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "offline interceptor: called.")
            var request = chain.request()

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!ApplicationInit.hasNetwork()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "network interceptor: called.")
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.SECONDS)
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG,
                "log: http log: $message")
        }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    val api: ApiInterface
        get() = retrofitdemo().create(ApiInterface::class.java)
}