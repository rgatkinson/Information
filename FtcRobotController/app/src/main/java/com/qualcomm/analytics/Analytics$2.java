package com.qualcomm.analytics;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

static final class Analytics$2 implements X509TrustManager {
    @Override
    public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
    }
    
    @Override
    public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}