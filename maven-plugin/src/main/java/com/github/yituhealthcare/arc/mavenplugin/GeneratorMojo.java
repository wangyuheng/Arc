package com.github.yituhealthcare.arc.mavenplugin;

import com.alibaba.fastjson.JSON;
import com.github.yituhealthcare.arc.generator.DgraphSchemaGenerator;
import com.github.yituhealthcare.arc.generator.JavaCodeGenerator;
import com.github.yituhealthcare.arc.generator.conf.CodeGenConfig;
import com.github.yituhealthcare.arc.generator.io.CodeWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 代码生成插件
 * 依赖 generator module
 *
 * @author yuheng.wang
 */
@Mojo(name = "generate")
public class GeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${basedir}/src/main/resources/arc-generator.json")
    private File configJson;
    @Parameter(defaultValue = "${basedir}/src/main/resources/graphql/schema.graphqls")
    private File schemaPath;
    @Parameter(property = "target", defaultValue = "all")
    private String target;

    private static final String TARGET_ALL = "all";
    private static final String TARGET_JAVA = "java";
    private static final String TARGET_DGRAPH = "dgraph";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final CodeGenConfig config = getConfig();
        if (TARGET_ALL.equalsIgnoreCase(target) || TARGET_JAVA.equalsIgnoreCase(target)) {
            generateJava(config);
        }
        if (TARGET_ALL.equalsIgnoreCase(target) || TARGET_DGRAPH.equalsIgnoreCase(target)) {
            generateDgraph(config);
        }
    }

    private CodeGenConfig getConfig() throws MojoFailureException {
        CodeGenConfig config;
        if (null == configJson || !configJson.exists()) {
            config = new CodeGenConfig();
            getLog().warn("You don't set config in ${basedir}/src/main/resources/arc-generator.json Arc Generator should run by default config. " + JSON.toJSONString(config));
        } else {
            try {
                config = JSON.parseObject(Files.readAllBytes(configJson.toPath()), CodeGenConfig.class);
            } catch (Exception e) {
                throw new MojoFailureException("load config fail!", e);
            }
        }
        return config;
    }

    private void generateJava(CodeGenConfig config) throws MojoFailureException, MojoExecutionException {
        try (InputStream schemaIn = new FileInputStream(schemaPath)) {
            CodeWriter codeWriter = new CodeWriter(Paths.get(".", "/src/main/java"));
            //wait json builder & union
            new JavaCodeGenerator(codeWriter, config).generate(schemaIn);
        } catch (IOException e) {
            throw new MojoExecutionException("generate java code fail!", e);
        }
    }

    private void generateDgraph(CodeGenConfig config) throws MojoExecutionException {
        try (InputStream schemaIn = new FileInputStream(schemaPath)) {
            List<String> sql = new DgraphSchemaGenerator().generate(schemaIn);
            Path dgraphPath = Paths.get(project.getBasedir().getPath(), "/src/main/resources", config.getDgraphPath());
            Files.createDirectories(dgraphPath.getParent());
            Files.write(dgraphPath, sql);
        } catch (IOException e) {
            throw new MojoExecutionException("generate dgraph schema fail!", e);
        }
    }
}