package com.github.yituhealthcare.arcfullsample;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@DgraphScan(basePackage = {"com.github.yituhealthcare.arcfullsample"})
@SpringBootApplication
public class FullSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullSampleApplication.class, args);
    }

}

