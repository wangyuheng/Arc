package io.github.wangyuheng.arc.dgraph;

import io.github.wangyuheng.arc.dgraph.datasource.DataSourceInitializer;
import io.github.wangyuheng.arc.dgraph.repository.DgraphClientAdapter;
import io.github.wangyuheng.arc.dgraph.repository.DgraphInterceptor;
import io.github.wangyuheng.arc.dgraph.repository.mapper.DgraphMapperManager;
import io.github.wangyuheng.arc.dgraph.scanner.AutoConfiguredDgraphScannerRegistrar;
import io.github.wangyuheng.arc.dgraph.trace.DgraphTracerFilter;
import io.dgraph.DgraphClient;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.sleuth.zipkin2.ZipkinAutoConfiguration;
import org.springframework.cloud.sleuth.zipkin2.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration}
 */
@Configuration
@AutoConfigurationPackage
public class DgraphAutoConfiguration {

    @Bean
    public AutoConfiguredDgraphScannerRegistrar autoConfiguredDgraphScannerRegistrar() {
        return new AutoConfiguredDgraphScannerRegistrar();
    }

    @Bean
    @ConfigurationProperties(prefix = "arc.dgraph")
    public DgraphProperties dgraphProperties() {
        return new DgraphProperties();
    }

    /**
     * {@link DgraphClient}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "arc.dgraph.urls")
    public DgraphClient dgraphClient(DgraphProperties properties) {
        return new DgraphClient(properties.getDgraphStubs());
    }

    @Bean
    @ConditionalOnMissingBean
    public DgraphMapperManager dgraphMapperManager() {
        return new DgraphMapperManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public DgraphClientAdapter dgraphClientAdapter(DgraphClient dgraphClient, List<DgraphInterceptor> interceptors) {
        return new DgraphClientAdapter(dgraphClient, interceptors);
    }
    /**
     * 依赖zipkin配置决定是否启动tracer
     *
     * @see ZipkinProperties#isEnabled()
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ZipkinAutoConfiguration.class)
    public DgraphTracerFilter dgraphTracerFilter() {
        return new DgraphTracerFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "arc.dgraph.init", havingValue = "true")
    public DataSourceInitializer dataSourceInitializer() {
        return new DataSourceInitializer();
    }
}
