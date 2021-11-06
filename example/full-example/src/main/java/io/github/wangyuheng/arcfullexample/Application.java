package io.github.wangyuheng.arcfullexample;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@DgraphScan(basePackage = {"io.github.wangyuheng.arcfullexample"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

