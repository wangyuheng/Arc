package io.github.wangyuheng.arc.dgraph.datasource;

import io.dgraph.DgraphClient;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Tests for {@link DataSourceInitializer}.
 * 初始化条件判断
 *
 * @author yuheng.wang
 */
public class DataSourceInitializerTest {

    @Test
    public void should_init_when_define_existed() throws Exception {
        DataSourceInitializer dataSourceInitializer = PowerMockito.spy(new DataSourceInitializer());
        DgraphClient dgraphClient = PowerMockito.mock(DgraphClient.class);
        PowerMockito.field(DataSourceInitializer.class, "dgraphClient").set(dataSourceInitializer, dgraphClient);
        PowerMockito.field(DataSourceInitializer.class, "schemaPath").set(dataSourceInitializer, this.mockClassPathResource(true));

        dataSourceInitializer.afterPropertiesSet();
        Mockito.verify(dgraphClient, Mockito.only()).alter(ArgumentMatchers.any());
    }

    @Test
    public void should_not_init_when_define_not_existed() throws Exception {
        DataSourceInitializer dataSourceInitializer = PowerMockito.spy(new DataSourceInitializer());
        DgraphClient dgraphClient = PowerMockito.mock(DgraphClient.class);
        PowerMockito.field(DataSourceInitializer.class, "dgraphClient").set(dataSourceInitializer, dgraphClient);
        PowerMockito.field(DataSourceInitializer.class, "schemaPath").set(dataSourceInitializer, this.mockClassPathResource(false));

        dataSourceInitializer.afterPropertiesSet();
        Mockito.verify(dgraphClient, Mockito.never()).alter(ArgumentMatchers.any());
    }

    @Test
    public void should_drop_and_create_schema_when_init_and_drop_all() throws Exception {
        DataSourceInitializer dataSourceInitializer = PowerMockito.spy(new DataSourceInitializer());
        DgraphClient dgraphClient = PowerMockito.mock(DgraphClient.class);
        PowerMockito.field(DataSourceInitializer.class, "dropAll").set(dataSourceInitializer, true);
        PowerMockito.field(DataSourceInitializer.class, "dgraphClient").set(dataSourceInitializer, dgraphClient);
        PowerMockito.field(DataSourceInitializer.class, "schemaPath").set(dataSourceInitializer, this.mockClassPathResource(true));

        dataSourceInitializer.afterPropertiesSet();
        Mockito.verify(dgraphClient, Mockito.times(2)).alter(ArgumentMatchers.any());
    }

    private ClassPathResource mockClassPathResource(boolean exists) throws IOException {
        ClassPathResource classPathResource = PowerMockito.mock(ClassPathResource.class);
        PowerMockito.when(classPathResource.exists()).thenReturn(exists);
        PowerMockito.when(classPathResource.getFile()).thenReturn(Files.createTempFile("dgraph", "schema").toFile());
        return classPathResource;
    }

}