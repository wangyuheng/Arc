package io.github.wangyuheng.arc.cli;

import com.alibaba.fastjson.JSONObject;
import io.github.wangyuheng.arc.generator.DgraphSchemaGenerator;
import io.github.wangyuheng.arc.generator.JavaCodeGenerator;
import io.github.wangyuheng.arc.generator.conf.CodeGenConfig;
import io.github.wangyuheng.arc.generator.io.CodeWriter;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Arc命令行工具
 *
 * @author yuheng.wang
 */
@CommandLine.Command(name = "arc",
        version = "软件名称：Arc 版本：1.6.0",
        description = "arc tool for generate code by graphql schema",
        mixinStandardHelpOptions = true
)
class GeneratorCli implements Callable<Integer> {

    @CommandLine.Option(names = {"-c", "--config"}, paramLabel = "FILE", description = "config json file")
    private File configJson;
    @CommandLine.Option(names = {"-s", "--schema"}, paramLabel = "FILE", description = "graphql schema file")
    private File schemaPath;
    @CommandLine.Option(names = {"-t", "--target"}, defaultValue = "all")
    private String target;

    private static final String TARGET_ALL = "all";
    private static final String TARGET_JAVA = "java";
    private static final String TARGET_DGRAPH = "dgraph";

    @Override
    public Integer call() throws Exception {
        final CodeGenConfig config = getConfig();
        if (TARGET_ALL.equalsIgnoreCase(target) || TARGET_JAVA.equalsIgnoreCase(target)) {
            generateJava(config);
        }
        if (TARGET_ALL.equalsIgnoreCase(target) || TARGET_DGRAPH.equalsIgnoreCase(target)) {
            generateDgraph(config);
        }
        return null;

    }

    private CodeGenConfig getConfig() {
        CodeGenConfig config;
        if (null == configJson || !configJson.exists()) {
            config = new CodeGenConfig();
            System.err.println("You don't set config in ${basedir}/src/main/resources/arc-generator.json Arc Generator should run by default config. " + JSONObject.toJSONString(config));
        } else {
            try {
                config = JSONObject.parseObject(Files.readAllBytes(configJson.toPath()), CodeGenConfig.class);
            } catch (Exception e) {
                throw new RuntimeException("load config fail!", e);
            }
        }
        return config;
    }

    private void generateJava(CodeGenConfig config) {
        try (InputStream schemaIn = new FileInputStream(schemaPath)) {
            CodeWriter codeWriter = new CodeWriter(Paths.get(".", "/src/main/java"));
            //wait json builder & union
            new JavaCodeGenerator(codeWriter, config).generate(schemaIn);
        } catch (IOException e) {
            throw new RuntimeException("generate java code fail!", e);
        }
    }

    private void generateDgraph(CodeGenConfig config) {
        try (InputStream schemaIn = new FileInputStream(schemaPath)) {
            List<String> sql = new DgraphSchemaGenerator().generate(schemaIn);
            Path dgraphPath = Paths.get("./a", "/src/main/resources", config.getDgraphPath());
            Files.createDirectories(dgraphPath.getParent());
            Files.write(dgraphPath, sql);
        } catch (IOException e) {
            throw new RuntimeException("generate dgraph schema fail!", e);
        }
    }
}


public class ArcCli {

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new GeneratorCli());
        if (args.length == 0) {
            cmd.usage(System.out);
        } else {
            int exitCode = cmd.execute(args);
            System.exit(exitCode);
        }
    }

}