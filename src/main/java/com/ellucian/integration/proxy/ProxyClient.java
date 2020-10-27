package com.ellucian.integration.proxy;

import com.ellucian.integration.BaseHttpClient;
import com.ellucian.integration.Configuration;
import com.ellucian.integration.auth.SessionToken;

import java.io.IOException;
import java.util.HashMap;

/**
 * Client class for making Proxy API calls
 */
public class ProxyClient {

    private SessionToken token;

    private static ProxyClient instance = new ProxyClient();

    public static ProxyClient getClient(SessionToken token) {
        instance.token = token;
        return instance;
    }

    private ProxyClient() {}

    // get a resource with optional version and id qualifiers
    public String get(String resource, String version, String id) throws IOException {
        // build URL
        String url = Configuration.PROXY_API_URL+"/"+resource;
        if (id != null && !id.isEmpty()) {
            url += "/"+id;
        }
        // determine accept type
        String type = (version != null && !version.isEmpty()) ? version : "application/json";
        // build headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + token.getToken());
        headers.put("accept", type);

        return BaseHttpClient.get(url, headers);
    }

    // post a resource with the given version
    public String post(String resource, String version, String body) throws IOException {
        // build headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + token.getToken());
        headers.put("content-type", "application/json");
        headers.put("accept", version);

        return BaseHttpClient.post(Configuration.PROXY_API_URL+"/"+resource, headers, body);
    }

    // post a resource with the given version
    public String queryByPost(String resource, String version, String body) throws IOException {
        // build headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + token.getToken());
        headers.put("content-type", version);
        headers.put("accept", version);

        return BaseHttpClient.post(Configuration.PROXY_QAPI_URL+"/"+resource, headers, body);
    }
}
