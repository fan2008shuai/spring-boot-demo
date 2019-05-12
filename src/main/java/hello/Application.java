package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty("server.port", "8090");

        System.setProperty("project.name", "org-fan-sentinel-demo");
        System.setProperty("csp.sentinel.dashboard.server", "localhost:8080");
        SpringApplication.run(Application.class, args);
    }
}
