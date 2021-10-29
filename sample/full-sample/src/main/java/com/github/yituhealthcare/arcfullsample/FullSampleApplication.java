package io.github.wangyuheng.arcfullsample;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@DgraphScan(basePackage = {"io.github.wangyuheng.arcfullsample"})
@SpringBootApplication
public class FullSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullSampleApplication.class, args);
    }

}

