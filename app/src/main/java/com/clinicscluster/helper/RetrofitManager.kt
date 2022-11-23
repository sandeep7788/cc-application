package com.clinicscluster.helper

import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

public class RetrofitManager {
    private var retrofit: Retrofit? = null

    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    val instance: Retrofit?
        get() {
            if (retrofit == null) {

                    try {
                        if (!isConnected()) {
                            Toast.makeText(ApplicationInit.instance, "Please check internet connection", Toast.LENGTH_LONG).show()
                        }

                    } catch (e:Exception) {

                    }
                synchronized(RetrofitManager::class.java) {
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    retrofit = Retrofit.Builder()
                        .baseUrl(ApiContants.PREF_base_url)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                }
            }
            return retrofit
        }



    val getDemo: Retrofit?
        get() {
            if (retrofit == null) {
                synchronized(RetrofitManager::class.java) {
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    retrofit = Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                }
            }
            return retrofit
        }

    private val okHttpClient = unsafeOkHttpClient.connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            //            builder.addHeader("content-type", "application/json");
            builder.addHeader("Access-Control-Allow-Origin", "api")
            builder.addHeader("APPKEY", "JsfnZGWj20NJMIyg2LDIvQ==")
            chain.proceed(builder.build())
        }

        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private var newRetrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client =
                unsafeOkHttpClient.addInterceptor(interceptor).readTimeout(80, TimeUnit.SECONDS)
                    .connectTimeout(80, TimeUnit.SECONDS).build()
            val gson = GsonBuilder().serializeNulls().create()
            newRetrofit = Retrofit.Builder()
                .baseUrl("url")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return newRetrofit
        }

    val googleClient: Retrofit
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client =
                unsafeOkHttpClient.addInterceptor(interceptor).readTimeout(80, TimeUnit.SECONDS)
                    .connectTimeout(80, TimeUnit.SECONDS).build()
            return Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

    // Create a trust manager that does not validate certificate chains
    val unsafeOkHttpClient: OkHttpClient

    // Install the all-trusting trust manager
    .Builder
        get() = try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}