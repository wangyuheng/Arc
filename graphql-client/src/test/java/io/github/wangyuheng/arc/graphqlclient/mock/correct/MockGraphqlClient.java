package io.github.wangyuheng.arc.graphqlclient.mock.correct;

import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlClient;
import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlMapping;
import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlParam;
import io.github.wangyuheng.arc.graphqlclient.mock.MockResponseData;
import io.github.wangyuheng.arc.graphqlclient.model.GraphqlResponse;

/**
 * test case for {@link io.github.wangyuheng.arc.graphqlclient.GraphqlClientsRegistrarTest}
 *
 * @author yuheng.wang
 */
@GraphqlClient(value = "mockGraphqlClient", url = "mock_url")
public interface MockGraphqlClient {

    @GraphqlMapping(path = "echo.graphql")
    GraphqlResponse<MockResponseData> mock(@GraphqlParam("uid") String id);
}
