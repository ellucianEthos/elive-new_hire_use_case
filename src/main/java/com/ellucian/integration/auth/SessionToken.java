package com.ellucian.integration.auth;

import com.ellucian.integration.BaseHttpClient;
import com.ellucian.integration.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * This class will manage a session token for an application's API key.  It will automatically refresh the token before
 * it expires.
 */
public class SessionToken {

    private String apiKey;

    private String token;

    private LocalDateTime expirationTime = LocalDateTime.now();

    public SessionToken(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getToken() {
        if (!isTokenValid()) {
            init();
        }
        return token;
    }

    private void init() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + Configuration.CLIENT_APP_API_KEY);
        try {
            token = BaseHttpClient.post(Configuration.AUTH_URL, headers, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isTokenValid() {
        LocalDateTime now = LocalDateTime.now();
        return (token != null && now.isBefore(expirationTime));
    }
}
