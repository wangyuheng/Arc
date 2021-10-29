package io.github.wangyuheng.arc.graphqlclient.mock.urlempty;

import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlClient;

/**
 * test case for {@link io.github.wangyuheng.arc.graphqlclient.GraphqlClientsRegistrarTest}
 *
 * @author yuheng.wang
 */
@GraphqlClient(value = "mockGraphqlClient", url = "")
public interface MockGraphqlClient {

}
