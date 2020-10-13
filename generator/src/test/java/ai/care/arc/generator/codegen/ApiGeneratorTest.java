package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.PackageManager;
import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ApiGenerator}.
 *
 * @author yuheng.wang
 */
public class ApiGeneratorTest {

    private ApiGenerator apiGenerator;
    private TypeDefinitionRegistry typeDefinitionRegistry;

    /**
     * scalar DateTime
     *
     * schema{
     *     query: Query,
     *     mutation: Mutation
     * }
     *
     * type Query{
     *     project(
     *         id: String
     *     ): Project
     * }
     *
     * type Mutation{
     *     createProject(
     *         payload: ProjectInput
     *     ): Project
     * }
     *
     * """
     * 项目
     * 为了达到某个产品迭代、产品模块开发、或者科研调研等目的所做的工作.
     * """
     * type Project{
     *     id: ID!
     *     name: String!
     *     description: String!
     *     category: [ProjectCategory]
     *     createTime: DateTime!
     *     milestones(
     *         status: MilestoneStatus
     *     ): [Milestone]
     * }
     * """
     * 里程碑
     * 表述一个Project的某个时间阶段及阶段性目标. 一个Project可以同时拥有多个处于相同或者不同阶段的Milestone.
     * """
     * type Milestone{
     *     id: String!
     *     name: String!
     * }
     */
    @Before
    public void setUp() throws Exception {
        String schema = "scalar DateTime\n" +
                "\n" +
                "schema{\n" +
                "    query: Query,\n" +
                "    mutation: Mutation\n" +
                "}\n" +
                "\n" +
                "type Query{\n" +
                "    project(\n" +
                "        id: String\n" +
                "    ): Project\n" +
                "}\n" +
                "\n" +
                "type Mutation{\n" +
                "    createProject(\n" +
                "        payload: ProjectInput\n" +
                "    ): Project\n" +
                "}\n" +
                "\n" +
                "\"\"\"\n" +
                "项目\n" +
                "为了达到某个产品迭代、产品模块开发、或者科研调研等目的所做的工作.\n" +
                "\"\"\"\n" +
                "type Project{\n" +
                "    id: ID!\n" +
                "    name: String!\n" +
                "    description: String!\n" +
                "    category: [ProjectCategory]\n" +
                "    createTime: DateTime!\n" +
                "    milestones(\n" +
                "        status: MilestoneStatus\n" +
                "    ): [Milestone]\n" +
                "}\n" +
                "\"\"\"\n" +
                "里程碑\n" +
                "表述一个Project的某个时间阶段及阶段性目标. 一个Project可以同时拥有多个处于相同或者不同阶段的Milestone.\n" +
                "\"\"\"\n" +
                "type Milestone{\n" +
                "    id: String!\n" +
                "    name: String!\n" +
                "}\n";

        typeDefinitionRegistry = new SchemaParser().parse(schema);
        apiGenerator = new ApiGenerator(new PackageManager("a.b.c", typeDefinitionRegistry));
    }

    @Test
    public void api_gen_e2e_test() {
        List<String> expected = Arrays.asList(
                "package a.b.c.api;" +
                        "import a.b.c.type.Milestone;" +
                        "import graphql.schema.DataFetchingEnvironment;" +
                        "import java.util.List;" +
                        "/**" +
                        " * Generate with GraphQL Schema By Arc" +
                        " */" +
                        "public interface ProjectService {" +
                        "  List<Milestone> handleMilestones(DataFetchingEnvironment dataFetchingEnvironment);" +
                        "}",
                "package a.b.c.api;" +
                        "import a.b.c.type.Project;" +
                        "import graphql.schema.DataFetchingEnvironment;" +
                        "/**" +
                        " * Generate with GraphQL Schema By Arc" +
                        " */" +
                        "public interface QueryService {" +
                        "  Project handleProject(DataFetchingEnvironment dataFetchingEnvironment);" +
                        "}",
                "package a.b.c.api;" +
                        "import a.b.c.type.Project;" +
                        "import graphql.schema.DataFetchingEnvironment;" +
                        "/**" +
                        " * Generate with GraphQL Schema By Arc" +
                        " */" +
                        "public interface MutationService {" +
                        "  Project handleCreateProject(DataFetchingEnvironment dataFetchingEnvironment);" +
                        "}"
        );
        assertEquals(apiGenerator.apply(typeDefinitionRegistry)
                        .map(JavaFile::toString)
                        .map(it -> it.replaceAll("\n", ""))
                        .collect(Collectors.toList())
                , expected);
    }

}