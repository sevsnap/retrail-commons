/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import static it.cnr.iit.retrail.commons.HttpsWebServer.sslContext;
import static it.cnr.iit.retrail.commons.Server.log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author oneadmin
 */
public class HttpsTrustManager {
    
    public static KeyManager[] getKeyManagers(InputStream ksIs, String password) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        assert (ksIs != null);
        KeyStore ks = KeyStore.getInstance("JKS");
        try {
            ks.load(ksIs, password == null ? null : password.toCharArray());
        } finally {
            if (ksIs != null) {
                ksIs.close();
            }
        }
        log.warn("getting KeyManagerFactory instance for algorithm: {}", KeyManagerFactory
                .getDefaultAlgorithm());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(ks, password == null ? null : password.toCharArray());
        return kmf.getKeyManagers();
    }

    public static TrustManager[] getTrustManagers() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    log.warn("called");
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Trust always
                    log.warn("trusting client for authType: {}", authType);
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Trust always
                    log.warn("trusting server for authType: {}", authType);
                }
            }
        };
        return trustAllCerts;
    }
   
    public static HostnameVerifier getHostnameVerifier() {
        // Create empty HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                log.warn("verifying hostname with dummy HostnameVerifier: {}", arg0);
                return true;
            }
        };
        return hv;
    }
    
}
