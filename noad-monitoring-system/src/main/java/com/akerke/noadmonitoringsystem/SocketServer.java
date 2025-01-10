package com.akerke.noadmonitoringsystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SocketServer implements Runnable {
    private final int port;
    private static final List<String> metricsList = Collections.synchronizedList(new ArrayList<>());

    public SocketServer(int port) {
        this.port = port;
    }

    public static List<String> getMetrics() {
        return new ArrayList<>(metricsList);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket server is running on port " + port);
            ExecutorService threadPool = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String metrics;
            while ((metrics = in.readLine()) != null) {
                System.out.println("Received metrics: " + metrics);
                metricsList.add(metrics);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
