package com.akerke.noadmonitoringsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class NoadMonitoringSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoadMonitoringSystemApplication.class, args);

        SocketServer socketServer = new SocketServer(9091); // Use port 9090 for socket communication
        new Thread(socketServer).start();
    }


    @RestController
    @RequestMapping("/metrics")
    class MetricsController {
        @GetMapping
        public List<String> getMetrics() {
            return SocketServer.getMetrics();
        }
    }
}
