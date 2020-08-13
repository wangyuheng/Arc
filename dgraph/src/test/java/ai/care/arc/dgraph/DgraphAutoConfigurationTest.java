package ai.care.arc.dgraph;

import io.dgraph.DgraphAsyncClient;
import io.dgraph.DgraphClient;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link DgraphAutoConfiguration}.
 * 相关bean初始化成功
 *
 * @author yuheng.wang
 */
@SpringBootTest
public class DgraphAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DgraphAutoConfiguration.class));

    @Test
    public void test_context_load() {
        this.contextRunner.withPropertyValues("arc.dgraph.urls=localhost:8080,localhost:8081")
                .run(context -> {
                    assertNotNull(context.getBean("autoConfiguredDgraphScannerRegistrar"));
                    assertNotNull(context.getBean("dgraphProperties"));
                    assertNotNull(context.getBean("dgraphClient"));
                    assertNotNull(context.getBean("dgraphMapperManager"));
                    assertNotNull(context.getBean("dgraphClientAdapter"));
                    assertNotNull(context.getBean("dgraphTracerFilter"));
                });
    }

    @Test
    public void should_init_dgraph_client_stubs_by_properties() throws IllegalAccessException {
        DgraphProperties dgraphProperties = new DgraphProperties();
        dgraphProperties.setUrls(Arrays.asList("localhost:8080", "localhost:8081"));
        DgraphClient dgraphClient = new DgraphAutoConfiguration().dgraphClient(dgraphProperties);

        DgraphAsyncClient dgraphAsyncClient = (DgraphAsyncClient) PowerMockito.field(DgraphClient.class, "asyncClient").get(dgraphClient);
        List<Object> stubs = (List<Object>) PowerMockito.field(DgraphAsyncClient.class, "stubs").get(dgraphAsyncClient);

        assertEquals(2, stubs.size());
    }

}