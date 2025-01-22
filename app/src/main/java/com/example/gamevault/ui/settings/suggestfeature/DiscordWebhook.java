package com.example.gamevault.ui.settings.suggestfeature;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class DiscordWebhook {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1331029599999623188/x1JF2SalC7kiMrURnYPsJixzVpS-1tL4bio_q0TwZnKijBgGWRX6y65IDgixy3Vc7S2l";
    public void sendMessage(String content) {
        OkHttpClient client = new OkHttpClient();

        // Create JSON payload
        JSONObject json = new JSONObject();
        try {
            json.put("content", content); // Message content
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create request body
        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        // Build request
        Request request = new Request.Builder()
                .url(WEBHOOK_URL)
                .post(body)
                .build();

        // Execute request
        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Message sent successfully!");
                } else {
                    System.out.println("Failed to send message: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}