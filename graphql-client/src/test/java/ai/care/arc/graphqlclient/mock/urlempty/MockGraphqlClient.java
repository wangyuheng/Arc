package ai.care.arc.graphqlclient.mock.urlempty;

import ai.care.arc.graphqlclient.annotation.GraphqlClient;

/**
 * test case for {@link ai.care.arc.graphqlclient.GraphqlClientsRegistrarTest}
 *
 * @author yuheng.wang
 */
@GraphqlClient(value = "mockGraphqlClient", url = "")
public interface MockGraphqlClient {

}
