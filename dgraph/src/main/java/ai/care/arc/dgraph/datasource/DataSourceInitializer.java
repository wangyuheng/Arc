package ai.care.arc.dgraph.datasource;

import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.util.List;

/**
 * 根据配置初始化Dgraph schema
 *
 * @author yuheng.wang
 */
@Slf4j
public class DataSourceInitializer implements InitializingBean {

    @Value("${arc.dgraph.define:dgraph/schema.dgraph}")
    private ClassPathResource schemaPath;
    @Value("${arc.dgraph.drop-all:false}")
    private boolean dropAll;
    @Value("${arc.dgraph.init:false}")
    private boolean init;

    @Autowired
    private DgraphClient dgraphClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (init) {
            if (schemaPath.exists()) {
                List<String> list = Files.readAllLines(schemaPath.getFile().toPath());
                DgraphProto.Operation operation = DgraphProto.Operation.newBuilder()
                        .setSchema(String.join("\n", list))
                        .setDropAll(dropAll)
                        .build();
                log.debug("init dgraph by schema:{}", String.join("\n", list));
                dgraphClient.alter(operation);
                log.info("init dgraph by schemaPath:{}, dropAll:{}", schemaPath, dropAll);
            } else {
                log.warn("dgraph schema is not found! path:{} ", schemaPath);
            }
        } else {
            // could not run this because do not register this bean when init set false
            log.info("not enbalbe init dgraph schema");
        }

    }

}
