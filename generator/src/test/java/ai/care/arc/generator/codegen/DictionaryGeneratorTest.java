package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.PackageManager;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DictionaryGenerator}.
 *
 * @author yuheng.wang
 */
public class DictionaryGeneratorTest {

    private DictionaryGenerator dictionaryGenerator;
    private TypeDefinitionRegistry typeDefinitionRegistry;

    /**
     * """
     * this is e1
     * """
     * enum e1 {
     * """
     * this is ev1
     * """
     * ev1
     * """
     * this is ev2
     * """
     * ev2
     * }
     */
    @Before
    public void setUp() throws Exception {
        String schema = "\"\"\"\n" +
                "this is e1\n" +
                "\"\"\"\n" +
                "enum e1 {\n" +
                "    \"\"\"\n" +
                "    this is ev1\n" +
                "    \"\"\"\n" +
                "    ev1\n" +
                "    \"\"\"\n" +
                "    this is ev2\n" +
                "    \"\"\"\n" +
                "    ev2\n" +
                "}";
        typeDefinitionRegistry = new SchemaParser().parse(schema);
        dictionaryGenerator = new DictionaryGenerator(new PackageManager("a.b.c", typeDefinitionRegistry));
    }

    @Test
    public void enum_gen_e2e_test() {
        String expected = "package a.b.c.dictionary;" +
                "/**" +
                " * this is e1" +
                " * Generate with GraphQL Schema By Arc" +
                " */" +
                "public enum e1 {" +
                "  /**" +
                "   * this is ev1" +
                "   */" +
                "  ev1," +
                "  /**" +
                "   * this is ev2" +
                "   */" +
                "  ev2" +
                "}";
        assertEquals(
                dictionaryGenerator.apply(typeDefinitionRegistry)
                        .findAny()
                        .orElseThrow(NullPointerException::new)
                        .toString()
                        .replaceAll("\n", ""), expected);
    }

}