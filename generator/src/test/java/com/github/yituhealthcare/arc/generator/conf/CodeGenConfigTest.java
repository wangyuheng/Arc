package com.github.yituhealthcare.arc.generator.conf;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CodeGenConfig}.
 *
 * @author yuheng.wang
 */
public class CodeGenConfigTest {

    @Test
    public void should_deserialize_by_json() {
        String json = "{\n" +
                "    \"basePackage\": \"com.github.yituhealthcare.arcgeneratorsample\",\n" +
                "    \"dropAll\": false,\n" +
                "    \"genStrategies\": [\n" +
                "        {\n" +
                "            \"codeGenOperation\": \"OVERRIDE\",\n" +
                "            \"codeGenType\": \"TYPE\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"codeGenOperation\": \"OVERRIDE\",\n" +
                "            \"codeGenType\": \"API\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"ignoreJavaFileNames\": [\n" +
                "        \"User\"\n" +
                "    ],\n" +
                "    \"dgraphPath\": \"dgraph/schema.dgraph\"\n" +
                "}";
        CodeGenConfig config = JSON.parseObject(json, CodeGenConfig.class);
        assertEquals(2, config.getGenStrategies().size());
    }

}