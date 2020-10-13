package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.PackageManager;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link InputGenerator}.
 *
 * @author yuheng.wang
 */
public class InputGeneratorTest {

    private InputGenerator inputGenerator;
    private TypeDefinitionRegistry typeDefinitionRegistry;

    /**
     * """
     * this is i1
     * """
     * input i1 {
     * """
     * this is iv1
     * """
     * iv1: String
     * """
     * this is iv2
     * """
     * iv2: Int
     * }
     */
    @Before
    public void setUp() throws Exception {
        String schema = "\"\"\"\n" +
                "this is i1\n" +
                "\"\"\"\n" +
                "input i1 {\n" +
                "    \"\"\"\n" +
                "    this is iv1\n" +
                "    \"\"\"\n" +
                "    iv1: String\n" +
                "    \"\"\"\n" +
                "    this is iv2\n" +
                "    \"\"\"\n" +
                "    iv2: Int\n" +
                "}\n";
        typeDefinitionRegistry = new SchemaParser().parse(schema);
        inputGenerator = new InputGenerator(new PackageManager("a.b.c", typeDefinitionRegistry));
    }

    @Test
    public void input_gen_e2e_test() {
        String expected = "package a.b.c.input;" +
                "" +
                "import java.lang.Integer;" +
                "import java.lang.String;" +
                "" +
                "/**" +
                " * this is i1" +
                " * Generate with GraphQL Schema By Arc" +
                " */" +
                "public class i1 {" +
                "  /**" +
                "   * this is iv1" +
                "   */" +
                "  private String iv1;" +
                "" +
                "  /**" +
                "   * this is iv2" +
                "   */" +
                "  private Integer iv2;" +
                "" +
                "  public void setIv1(String iv1) {" +
                "    this.iv1 = iv1;" +
                "  }" +
                "" +
                "  public String getIv1() {" +
                "    return iv1;" +
                "  }" +
                "" +
                "  public void setIv2(Integer iv2) {" +
                "    this.iv2 = iv2;" +
                "  }" +
                "" +
                "  public Integer getIv2() {" +
                "    return iv2;" +
                "  }" +
                "}";
        assertEquals(inputGenerator.apply(typeDefinitionRegistry).findAny()
                .orElseThrow(NullPointerException::new)
                .toString()
                .replaceAll("\n", ""), expected);
    }
}