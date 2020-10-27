package com.ellucian.integration.consume;

import com.ellucian.integration.BaseHttpClient;
import com.ellucian.integration.Configuration;
import com.ellucian.integration.auth.SessionToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Client class for consuming change-notifications
 */
public class ConsumeClient {

    private SessionToken token;

    private static final String cnType = "application/vnd.hedtech.change-notifications.v2+json";

    // number of seconds to wait before checking for more CN's
    private int pollingInterval = 1;

    private static ConsumeClient instance = new ConsumeClient();

    public static ConsumeClient getClient(SessionToken token) {
        instance.token = token;
        return instance;
    }

    private ConsumeClient() {}

    // this will continuously poll for messages (CN's) and print them to the console as they are retrieved
    public void pollForMessages() throws InterruptedException {
        while (true) {
            // build headers
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token.getToken());
            headers.put("Accept", cnType);

            try {
                String messages = BaseHttpClient.get(Configuration.CONSUME_URL, headers);
                JSONArray array = new JSONArray(messages);
                if(array.length() > 0) {
                    System.out.println();
                    System.out.println("RETRIEVED MESSAGES:");
                }
                for(int i = 0; i < array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    System.out.println("Message ID: " + obj.getInt("id"));
                    System.out.println(obj.toString(3));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Thread.sleep(pollingInterval*1000);
        }
    }
}
