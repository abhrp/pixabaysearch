package com.github.abhrp.pixabaysearchdemo.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.abhrp.pixabaysearchdemo.network.interfaces.Pixabay;
import com.github.abhrp.pixabaysearchdemo.network.response.PixabayPhotoResponse;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by abhrp on 4/30/17.
 */

public class NetworkConfig {
    private static NetworkConfig networkConfig;
    private Retrofit mRetrofitClient;
    private OkHttpClient mOkHttpClient;
    private static final String TAG = "NetworkConfig";
    private final String BASE_URL = "https://pixabay.com/";
    private Pixabay pixabay;

    private NetworkConfig() {}

    public synchronized static NetworkConfig getNetworkConfig() {
        if (networkConfig == null) {
            networkConfig = new NetworkConfig();
        }
        return networkConfig;
    }

    public Retrofit getRetrofitClient() {
        return mRetrofitClient;
    }

    public void configNetworkClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

                /**
                 * Checks whether the specified certificate chain (partial or complete) can
                 * be validated and is trusted for client authentication for the specified
                 * authentication type.
                 *
                 * @param chain    the certificate chain to validate.
                 * @param authType the authentication type used.
                 * @throws CertificateException     if the certificate chain can't be validated or isn't trusted.
                 * @throws IllegalArgumentException if the specified certificate chain is empty or {@code null},
                 *                                  or if the specified authentication type is {@code null} or an
                 *                                  empty string.
                 */
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                /**
                 * Checks whether the specified certificate chain (partial or complete) can
                 * be validated and is trusted for server authentication for the specified
                 * key exchange algorithm.
                 *
                 * @param chain    the certificate chain to validate.
                 * @param authType the key exchange algorithm name.
                 * @throws CertificateException     if the certificate chain can't be validated or isn't trusted.
                 * @throws IllegalArgumentException if the specified certificate chain is empty or {@code null},
                 *                                  or if the specified authentication type is {@code null} or an
                 *                                  empty string.
                 */
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            }};
            sslContext.init(null, certs, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            mOkHttpClient = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).addNetworkInterceptor(new StethoInterceptor()).connectTimeout(60L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS).addInterceptor(getHttpLoggingInterceptor()).build();

            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } catch (Exception e) {
            Log.e(TAG, "configNetworkClient: ", e);
        }
    }

    public void cancelAllRequests() {
        mOkHttpClient.dispatcher().cancelAll();
    }

    @NonNull
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public void getPhotos(Map<String, Object> params, Callback<PixabayPhotoResponse> responseCallback) {
        if (pixabay == null) {
            pixabay = mRetrofitClient.create(Pixabay.class);
        }
        pixabay.getPhotos(params).enqueue(responseCallback);
    }
}
