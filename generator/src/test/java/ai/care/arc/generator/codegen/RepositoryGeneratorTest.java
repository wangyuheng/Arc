package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.PackageManager;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link RepositoryGenerator}.
 *
 * @author yuheng.wang
 */
public class RepositoryGeneratorTest {


    private RepositoryGenerator repositoryGenerator;
    private TypeDefinitionRegistry typeDefinitionRegistry;

    /**
     * scalar DateTime
     * <p>
     * schema{
     * query: Query,
     * mutation: Mutation
     * }
     * <p>
     * type Query{
     * project(
     * id: String
     * ): Project
     * }
     * <p>
     * type Mutation{
     * createProject(
     * payload: ProjectInput
     * ): Project
     * }
     * <p>
     * """
     * 项目
     * 为了达到某个产品迭代、产品模块开发、或者科研调研等目的所做的工作.
     * """
     * type Project{
     * id: ID!
     * name: String!
     * description: String!
     * category: [ProjectCategory]
     * createTime: DateTime!
     * milestones(
     * status: MilestoneStatus
     * ): [Milestone]
     * }
     * """
     * 里程碑
     * 表述一个Project的某个时间阶段及阶段性目标. 一个Project可以同时拥有多个处于相同或者不同阶段的Milestone.
     * """
     * type Milestone{
     * id: String!
     * name: String!
     * }
     */
    @Before
    public void setUp() throws Exception {
        String schema = "scalar DateTime\n" +
                "                \n" +
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
                "}";
        typeDefinitionRegistry = new SchemaParser().parse(schema);
        repositoryGenerator = new RepositoryGenerator(new PackageManager("a.b.c", typeDefinitionRegistry));
    }

    @Test
    public void repo_gen_e2e_test() {
        System.out.println(repositoryGenerator.apply(typeDefinitionRegistry).findAny()
                .orElseThrow(NullPointerException::new)
                .toString());
        String expected = "package a.b.c.repo;" +
                "import a.b.c.type.Project;" +
                "import ai.care.arc.dgraph.repository.SimpleDgraphRepository;" +
                "import org.springframework.stereotype.Repository;" +
                "/**" +
                " * Generate with GraphQL Schema By Arc" +
                " */" +
                "@Repository" +
                "public class ProjectRepository extends SimpleDgraphRepository<Project> {" +
                "}";
        assertEquals(repositoryGenerator.apply(typeDefinitionRegistry).findAny()
                .orElseThrow(NullPointerException::new)
                .toString()
                .replaceAll("\n", ""), expected);
    }
}