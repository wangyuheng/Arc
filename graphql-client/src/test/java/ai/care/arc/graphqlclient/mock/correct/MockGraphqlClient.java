package ai.care.arc.graphqlclient.mock.correct;

import ai.care.arc.graphqlclient.annotation.GraphqlClient;
import ai.care.arc.graphqlclient.annotation.GraphqlMapping;
import ai.care.arc.graphqlclient.annotation.GraphqlParam;
import ai.care.arc.graphqlclient.mock.MockResponseData;
import ai.care.arc.graphqlclient.model.GraphqlResponse;

/**
 * test case for {@link ai.care.arc.graphqlclient.GraphqlClientsRegistrarTest}
 *
 * @author yuheng.wang
 */
@GraphqlClient(value = "mockGraphqlClient", url = "mock_url")
public interface MockGraphqlClient {

    @GraphqlMapping(path = "echo.graphql")
    GraphqlResponse<MockResponseData> mock(@GraphqlParam("uid") String id);
}
