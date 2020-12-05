package com.github.yituhealthcare.arc.graphqlclient.mock.correct;

import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlClient;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlMapping;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlParam;
import com.github.yituhealthcare.arc.graphqlclient.mock.MockResponseData;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlResponse;

/**
 * test case for {@link com.github.yituhealthcare.arc.graphqlclient.GraphqlClientsRegistrarTest}
 *
 * @author yuheng.wang
 */
@GraphqlClient(value = "mockGraphqlClient", url = "mock_url")
public interface MockGraphqlClient {

    @GraphqlMapping(path = "echo.graphql")
    GraphqlResponse<MockResponseData> mock(@GraphqlParam("uid") String id);
}
