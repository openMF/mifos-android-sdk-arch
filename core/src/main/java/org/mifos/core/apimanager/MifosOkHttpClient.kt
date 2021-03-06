package org.mifos.core.apimanager

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import org.mifos.core.MifosPreferenceManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by grandolf49 on 18-06-2020
 *
 * OkHttpClient to disable SSL certificate validation in Retrofit
 */
object MifosOkHttpClient {

    val mifosOkHttpClient: OkHttpClient
        get() = try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                    if (MifosPreferenceManager.isAuthenticated()) {
                        newRequest.addHeader(
                            "Authorization",
                            "Bearer ${MifosPreferenceManager.getToken()}"
                        )
                    }
                    chain.proceed(newRequest.build())
                }
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}