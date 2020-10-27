package com.ellucian;

import com.ellucian.integration.Configuration;
import com.ellucian.integration.auth.SessionToken;
import com.ellucian.integration.consume.ConsumeClient;

public class ConsumeMessages {

    public static void main(String[] args) {
        SessionToken token = new SessionToken(Configuration.CLIENT_APP_API_KEY);
        ConsumeClient client = ConsumeClient.getClient(token);

        // start polling for new messages
        try {
            System.out.println("Polling for messages...");
            client.pollForMessages();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
