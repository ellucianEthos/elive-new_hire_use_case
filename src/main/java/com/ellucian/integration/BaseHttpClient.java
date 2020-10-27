package com.ellucian.integration;


import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class BaseHttpClient {

    // Create a custom response handler
    private static ResponseHandler<String> responseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            String responseBody = response.getEntity().getContentLength() > 0 ?
                    EntityUtils.toString(response.getEntity()) : response.getStatusLine().getReasonPhrase();
            throw new HttpResponseException(status, responseBody);
        }
    };

    // attach headers to your request object
    private static void attachHeaders(HttpRequestBase request, Map<String, String> headers) {
        for (String headerName : headers.keySet()) {
            request.addHeader(headerName, headers.getOrDefault(headerName, ""));
        }
    }

    // send a POST request
    public static String post(String url, Map<String, String> headers, String body) throws IOException {
        HttpPost post = new HttpPost(url);
        // add headers, if any
        if (headers != null && !headers.isEmpty()) {
            attachHeaders(post, headers);
        }
        // add the body, if there is one
        if (body != null) {
            StringEntity entity = new StringEntity(body);
            post.setEntity(entity);
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return httpClient.execute(post, responseHandler);
        }
    }

    // send a GET request
    public static String get(String url, Map<String, String> headers) throws IOException {
        HttpGet get = new HttpGet(url);
        // add headers, if any
        if ((headers != null) && (!headers.isEmpty())) {
            attachHeaders(get, headers);
        }

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            return httpclient.execute(get, responseHandler);
        }
    }
}
