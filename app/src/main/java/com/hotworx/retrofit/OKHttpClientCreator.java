package com.hotworx.retrofit;


import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.hotworx.helpers.BasePreferenceHelper;
import com.hotworx.helpers.UtilsHelpers;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.UUID;
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

    public static OkHttpClient createMyCustomForPreLollipop(Context context,Boolean isBrivoApi) {
        String version = "";
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        BasePreferenceHelper prefHelper = new BasePreferenceHelper(context);

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (
                PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            SSLContext sslContext = SSLConfigUtil.getSSLConfig(context);
            String finalVersion = version;
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
                            Request request = null;
                            try {
                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("sec-ch-ua-platform", "Android")
                                        .addHeader("application-version", finalVersion)
                                        .addHeader("device-id", UtilsHelpers.Companion.getDeviceId(context));
                                request = requestBuilder.build();
                            } catch (Exception ex) {
                            }

                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(logging);

            if (!isBrivoApi) {
//                builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            }

            client = builder.build();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
}
