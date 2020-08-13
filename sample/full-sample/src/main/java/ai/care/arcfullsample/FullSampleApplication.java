package ai.care.arcfullsample;

import ai.care.arc.dgraph.annotation.DgraphScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@DgraphScan(basePackage = {"ai.care.arcfullsample"})
@SpringBootApplication
public class FullSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullSampleApplication.class, args);
    }

}

