package com.github.yituhealthcare.arcgraphqlclientsample;

import com.github.yituhealthcare.arc.graphqlclient.annotation.EnableGraphqlClients;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlClient;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlMapping;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlParam;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuheng.wang
 */
@EnableGraphqlClients(basePackages = "com.github.yituhealthcare.arcgraphqlclientsample")
@RestController
@SpringBootApplication
public class ArcGraphqlClientSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArcGraphqlClientSampleApplication.class, args);
    }

    @Autowired
    private GitlabGraphqlClient gitlabGraphqlClient;

    @RequestMapping("rest")
    public String rest(@RequestParam(required = false, defaultValue = "Arc") String name) {
        return gitlabGraphqlClient.echo("Hello " + name).getData();
    }

    /**
     * For more information about gitlab graphql api see: https://gitlab.com/-/graphql-explorer
     */
    @GraphqlClient(value = "gitlabGraphqlClient", url = "https://gitlab.com/api/graphql")
    interface GitlabGraphqlClient {

        @GraphqlMapping(path = "ql/echo.graphql")
        GraphqlResponse<String> echo(@GraphqlParam("echoText") String text);

    }

}

