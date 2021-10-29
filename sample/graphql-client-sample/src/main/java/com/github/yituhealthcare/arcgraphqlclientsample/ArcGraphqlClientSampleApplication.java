package io.github.wangyuheng.arcgraphqlclientsample;

import io.github.wangyuheng.arc.graphqlclient.annotation.EnableGraphqlClients;
import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlClient;
import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlMapping;
import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlParam;
import io.github.wangyuheng.arc.graphqlclient.model.GraphqlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuheng.wang
 */
@EnableGraphqlClients(basePackages = "io.github.wangyuheng.arcgraphqlclientsample")
@RestController
@SpringBootApplication
public class ArcGraphqlClientSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArcGraphqlClientSampleApplication.class, args);
    }

    @Autowired
    private GitlabGraphqlClient gitlabGraphqlClient;

    @RequestMapping("rest")
    public EchoData rest(@RequestParam(required = false, defaultValue = "Arc") String name) {
        return gitlabGraphqlClient.echo("Hello " + name).getData();
    }

    /**
     * For more information about gitlab graphql api see: https://gitlab.com/-/graphql-explorer
     */
    @GraphqlClient(value = "gitlabGraphqlClient", url = "https://gitlab.com/api/graphql")
    interface GitlabGraphqlClient {

        @GraphqlMapping(path = "ql/echo.graphql")
        GraphqlResponse<EchoData> echo(@GraphqlParam("echoText") String text);

    }

    static class EchoData {
        private String echo;

        public String getEcho() {
            return echo;
        }

        public void setEcho(String echo) {
            this.echo = echo;
        }
    }

}