package com.akerke.node;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.Socket;

@SpringBootApplication
@EnableScheduling
public class NodeApplication implements CommandLineRunner {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9091;
    private final MeterRegistry meterRegistry;

    public NodeApplication(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public static void main(String[] args) {
        SpringApplication.run(NodeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Node is running and sending metrics to the server...");
    }

    @Scheduled(fixedRate = 5000)
    public void collectAndSendMetrics() {
        String metrics = collectMetrics();
        sendMetricsToServer(metrics);
    }

    private String collectMetrics() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = osBean.getSystemLoadAverage();
        int activeThreads = Thread.activeCount();

        // Register metrics with Prometheus
        meterRegistry.gauge("node.cpu.load", cpuLoad);
        meterRegistry.gauge("node.active.threads", activeThreads);

        return String.format("CPU Load: %.2f, Active Threads: %d", cpuLoad, activeThreads);
    }

    private void sendMetricsToServer(String metrics) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
            writer.println(metrics);
            System.out.println("Metrics sent: " + metrics);
        } catch (Exception e) {
            System.err.println("Failed to send metrics: " + e.getMessage());
        }
    }
}

