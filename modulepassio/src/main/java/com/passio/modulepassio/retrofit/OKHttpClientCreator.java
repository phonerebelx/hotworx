package com.passio.modulepassio.retrofit;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.passio.modulepassio.helpers.BasePreferenceHelper;
import com.passio.modulepassio.helpers.UtilsHelpers;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpClientCreator {

    public static OkHttpClient createMyCustomForPreLollipop(Context context, Boolean isBrivoApi) {
        String version = "unknown";  // Default version
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        BasePreferenceHelper prefHelper = new BasePreferenceHelper(context);

        // Set desired log level for the interceptor
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // Configure SSL context
            SSLContext sslContext = SSLConfigUtil.getSSLConfig(context);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            if (!(trustManagers.length == 1 && trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers: " + Arrays.toString(trustManagers));
            }

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .addHeader("sec-ch-ua-platform", "Android")
                                    .addHeader("application-version", "version")
                                    .addHeader("device-id", UtilsHelpers.Companion.getDeviceId(context));

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(logging);

            if (!isBrivoApi) {
                // Apply SSL context only if not Brivo API
                builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            }

            return builder.build();

        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
            throw new RuntimeException("Error configuring SSL", e);
        }
    }
}
