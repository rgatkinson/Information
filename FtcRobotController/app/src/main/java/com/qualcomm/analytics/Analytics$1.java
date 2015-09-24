package com.qualcomm.analytics;

import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;

static final class Analytics$1 implements HostnameVerifier {
    @Override
    public boolean verify(final String s, final SSLSession sslSession) {
        return true;
    }
}