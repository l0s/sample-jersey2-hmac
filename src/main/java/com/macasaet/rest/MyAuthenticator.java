package com.macasaet.rest;

import static java.util.concurrent.TimeUnit.MINUTES;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.auth.hmac.common.Credentials;
import com.bazaarvoice.auth.hmac.common.SignatureGenerator;
import com.bazaarvoice.auth.hmac.server.AbstractCachingAuthenticator;

public class MyAuthenticator extends AbstractCachingAuthenticator<String> {

    /*
     * Generated the private/public key pair like so:
     *
     * openssl genrsa -out private.pem 32
     * openssl rsa -in private.pem -pubout -out public.pem
     */
    private static final String onlyValidPrincipal = "Alice";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MyAuthenticator() {
        super(4, 0, MINUTES, 1024);
    }

    protected String loadPrincipal(final Credentials credentials) {
        if ("MCAwDQYJKoZIhvcNAQEBBQADDwAwDAIFAMbwciUCAwEAAQ==".equals(credentials.getApiKey())) {
            return onlyValidPrincipal;
        }
        return null;
    }

    protected String getSecretKeyFromPrincipal(final String principal) {
        if (onlyValidPrincipal.equals(principal)) {
            return "MC0CAQACBQDG8HIlAgMBAAECBBiRlekCAwDiuwIDAOCfAgMAlJcCAk3pAgMAkbI=";
        }
        return null;
    }

}