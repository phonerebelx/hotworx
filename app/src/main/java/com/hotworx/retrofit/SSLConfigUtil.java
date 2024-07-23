package com.hotworx.retrofit;

import android.content.Context;

import com.hotworx.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLConfigUtil {
    public static SSLContext getSSLConfig(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cert = context.getResources().openRawResource(R.raw.star_hotworx_net);
            X509Certificate ca = null;
            try {
                ca = (X509Certificate) cf.generateCertificate(cert);
            } finally {
                cert.close();
            }

            // Creating a KeyStore containing our trusted CA
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Creating a TrustManager that trusts the CAs in our KeyStore.
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
