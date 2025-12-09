package com.mycompany.app;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define endpoint "/"
        server.createContext("/", exchange -> {
            String response = "Hello from Kubernetes!";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        // Start the server
        server.start();
        System.out.println("Server started on port 8080");

        // Keep the main thread alive so the container doesn't exit
        Thread.currentThread().join();
    }
}
