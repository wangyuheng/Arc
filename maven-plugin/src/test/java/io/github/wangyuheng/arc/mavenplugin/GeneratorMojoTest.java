package io.github.wangyuheng.arc.mavenplugin;


import io.github.wangyuheng.arc.generator.conf.CodeGenConfig;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

/**
 * Tests for {@link GeneratorMojo}.
 *
 * @author yuheng.wang
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GeneratorMojo.class})
@Ignore
public class GeneratorMojoTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_throw_when_generate_java_if_schema_ioe() throws Exception {
        GeneratorMojo mojo = new GeneratorMojo();
        PowerMockito.field(GeneratorMojo.class, "schemaPath").set(mojo, Paths.get("not_existed").toFile());
        PowerMockito.field(GeneratorMojo.class, "target").set(mojo, "all");
        thrown.expect(MojoExecutionException.class);
        thrown.expectMessage("generate java code fail!");
        Whitebox.invokeMethod(mojo, "generateJava", new CodeGenConfig());
    }

    @Test
    public void should_throw_when_generate_dgraph_if_schema_ioe() throws Exception {
        GeneratorMojo mojo = new GeneratorMojo();
        PowerMockito.field(GeneratorMojo.class, "schemaPath").set(mojo, Paths.get("not_existed").toFile());
        PowerMockito.field(GeneratorMojo.class, "target").set(mojo, "all");
        thrown.expect(MojoExecutionException.class);
        thrown.expectMessage("generate dgraph schema fail!");
        Whitebox.invokeMethod(mojo, "generateDgraph", new CodeGenConfig());
    }

    @Test
    public void should_get_default_config_when_json_not_existed() throws Exception {
        GeneratorMojo mojo = new GeneratorMojo();
        CodeGenConfig config = Whitebox.invokeMethod(mojo, "getConfig");
        assertEquals(config, new CodeGenConfig());
    }

    @Test
    public void should_config_by_json() throws Exception {
        GeneratorMojo mojo = new GeneratorMojo();
        PowerMockito.field(GeneratorMojo.class, "configJson").set(mojo, new ClassPathResource("test_config.json").getFile());
        CodeGenConfig config = Whitebox.invokeMethod(mojo, "getConfig");
        assertNotEquals(config, new CodeGenConfig());
        // define in test_config.json
        assertEquals("io.github.wangyuheng.arc", config.getBasePackage());
    }

    @Test
    public void should_throw_if_config_json_read_fail() throws MojoFailureException, MojoExecutionException, IOException, IllegalAccessException {
        GeneratorMojo mojo = new GeneratorMojo();
        PowerMockito.field(GeneratorMojo.class, "configJson").set(mojo, new ClassPathResource("test_error_config").getFile());
        thrown.expect(MojoFailureException.class);
        thrown.expectMessage("load config fail!");
        mojo.execute();
    }

    @Test
    public void should_invoke_all_when_target_all() throws Exception {
        GeneratorMojo mojo = mock("all");
        mojo.execute();
        verifyPrivate(mojo, new Times(1)).invoke("generateJava", any(CodeGenConfig.class));
        verifyPrivate(mojo, new Times(1)).invoke("generateDgraph", any(CodeGenConfig.class));
    }

    @Test
    public void should_invoke_java_only_when_target_java() throws Exception {
        GeneratorMojo mojo = mock("java");
        mojo.execute();
        verifyPrivate(mojo, new Times(1)).invoke("generateJava", any(CodeGenConfig.class));
        verifyPrivate(mojo, new Times(0)).invoke("generateDgraph", any(CodeGenConfig.class));
    }

    @Test
    public void should_invoke_dgraph_only_when_target_dgraph() throws Exception {
        GeneratorMojo mojo = mock("dgraph");
        mojo.execute();
        verifyPrivate(mojo, new Times(0)).invoke("generateJava", any(CodeGenConfig.class));
        verifyPrivate(mojo, new Times(1)).invoke("generateDgraph", any(CodeGenConfig.class));
    }

    @Test
    public void should_ignore_target_case() throws Exception {
        GeneratorMojo mojo = mock("DgRaPh");
        mojo.execute();
        verifyPrivate(mojo, new Times(0)).invoke("generateJava", any(CodeGenConfig.class));
        verifyPrivate(mojo, new Times(1)).invoke("generateDgraph", any(CodeGenConfig.class));
    }

    private GeneratorMojo mock(String target) throws Exception {
        GeneratorMojo mojo = PowerMockito.spy(new GeneratorMojo());
        doNothing().when(mojo, "generateDgraph", any(CodeGenConfig.class));
        doNothing().when(mojo, "generateJava", any(CodeGenConfig.class));
        PowerMockito.field(GeneratorMojo.class, "target").set(mojo, target);
        return mojo;
    }
}