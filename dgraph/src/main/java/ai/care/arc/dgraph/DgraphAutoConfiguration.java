package ai.care.arc.dgraph;

import ai.care.arc.dgraph.repository.DgraphClientAdapter;
import ai.care.arc.dgraph.repository.DgraphInterceptor;
import ai.care.arc.dgraph.repository.mapper.DgraphMapperManager;
import ai.care.arc.dgraph.scanner.AutoConfiguredDgraphScannerRegistrar;
import ai.care.arc.dgraph.trace.DgraphTracerFilter;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        return new DgraphClient(
                properties.getAddresses().stream()
                        .map(address -> ManagedChannelBuilder
                                .forAddress(address.getName(), address.getPort())
                                .usePlaintext()
                                .build())
                        .map(DgraphGrpc::newStub)
                        .toArray(DgraphGrpc.DgraphStub[]::new)
        );
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
}
