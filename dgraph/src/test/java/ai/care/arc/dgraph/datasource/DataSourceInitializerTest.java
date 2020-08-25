package ai.care.arc.dgraph.datasource;

import io.dgraph.DgraphClient;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;

/**
 * Tests for {@link DataSourceInitializer}.
 * 初始化条件判断
 *
 * @author yuheng.wang
 */
public class DataSourceInitializerTest {

    @Test
    public void should_init_when_init_true_and_define_existed() throws Exception {
        DataSourceInitializer dataSourceInitializer = PowerMockito.spy(new DataSourceInitializer());
        DgraphClient dgraphClient = PowerMockito.mock(DgraphClient.class);
        PowerMockito.field(DataSourceInitializer.class, "init").setBoolean(dataSourceInitializer, true);
        PowerMockito.field(DataSourceInitializer.class, "dgraphClient").set(dataSourceInitializer, dgraphClient);

        ClassPathResource classPathResource = PowerMockito.mock(ClassPathResource.class);
        PowerMockito.when(classPathResource.exists()).thenReturn(true);
        PowerMockito.when(classPathResource.getFile()).thenReturn(Files.createTempFile("dgraph", "schema").toFile());
        PowerMockito.field(DataSourceInitializer.class, "schemaPath").set(dataSourceInitializer, classPathResource);

        dataSourceInitializer.afterPropertiesSet();
        Mockito.verify(dgraphClient, Mockito.only()).alter(ArgumentMatchers.any());
    }

    @Test
    public void should_not_init_when_init_true_but_define_not_existed() throws Exception {
        DataSourceInitializer dataSourceInitializer = PowerMockito.spy(new DataSourceInitializer());
        DgraphClient dgraphClient = PowerMockito.mock(DgraphClient.class);
        PowerMockito.field(DataSourceInitializer.class, "init").setBoolean(dataSourceInitializer, true);
        PowerMockito.field(DataSourceInitializer.class, "dgraphClient").set(dataSourceInitializer, dgraphClient);

        ClassPathResource classPathResource = PowerMockito.mock(ClassPathResource.class);
        PowerMockito.when(classPathResource.exists()).thenReturn(false);
        PowerMockito.field(DataSourceInitializer.class, "schemaPath").set(dataSourceInitializer, classPathResource);

        dataSourceInitializer.afterPropertiesSet();
        Mockito.verify(dgraphClient, Mockito.never()).alter(ArgumentMatchers.any());
    }

    @Test
    public void should_not_init_when_init_is_false() throws Exception {
        DataSourceInitializer dataSourceInitializer = PowerMockito.spy(new DataSourceInitializer());
        DgraphClient dgraphClient = PowerMockito.mock(DgraphClient.class);
        PowerMockito.field(DataSourceInitializer.class, "init").setBoolean(dataSourceInitializer, false);
        PowerMockito.field(DataSourceInitializer.class, "dgraphClient").set(dataSourceInitializer, dgraphClient);

        dataSourceInitializer.afterPropertiesSet();
        Mockito.verify(dgraphClient, Mockito.never()).alter(ArgumentMatchers.any());
    }

}