package io.github.wangyuheng.arc.generator;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DgraphSchemaGenerator}.
 * 根据GraphqlSchema生成DgraphSchema
 * 相关测试文件放在 test:resources目录下
 *
 * @author yuheng.wang
 */
public class DgraphSchemaGeneratorTest {

    @Test
    public void generate_dgraph_schema_by_graphql_schema() throws IOException {
        List<String> expected = Files.readAllLines(new ClassPathResource("schema.dgraph").getFile().toPath());
        List<String> sql = new DgraphSchemaGenerator().generate(new ClassPathResource("schema.graphqls").getInputStream());
        assertEquals(expected, sql);
    }

}