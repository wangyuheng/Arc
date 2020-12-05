package ai.care.arc.dgraph.datasource;

import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;

/**
 * 根据配置初始化Dgraph schema
 *
 * @author yuheng.wang
 */
public class DataSourceInitializer implements InitializingBean {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DataSourceInitializer.class);
    @Value("${arc.dgraph.define:dgraph/schema.dgraph}")
    private ClassPathResource schemaPath;
    @Value("${arc.dgraph.drop-all:false}")
    private boolean dropAll;

    @Autowired
    private DgraphClient dgraphClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (schemaPath.exists()) {
            final String schema = String.join("\n", Files.readAllLines(schemaPath.getFile().toPath()));
            if (dropAll) {
                log.debug("drop all data and schema");
                dgraphClient.alter(DgraphProto.Operation.newBuilder().setDropAll(true).build());
            }
            log.debug("init dgraph by schema:{}", schema);
            dgraphClient.alter(DgraphProto.Operation.newBuilder().setSchema(schema).build());
            log.info("init dgraph by schemaPath:{}, dropAll:{}", schemaPath, dropAll);
        } else {
            log.warn("dgraph schema is not found! path:{} ", schemaPath);
        }
    }

}