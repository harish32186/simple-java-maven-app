package com.mycompany.app;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void testHttpServerResponse() throws Exception {
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                App.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true); // ensures test exits even if server is running
        serverThread.start();

        // Wait a moment for the server to start
        Thread.sleep(1000);

        // Make HTTP request to the server
        URL url = new URL("http://localhost:8080/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        assertEquals("Hello from Kubernetes!", content.toString());
    }
}
