package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.PackageManager;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link TypeGenerator}.
 *
 * @author yuheng.wang
 */
public class TypeGeneratorTest {


    private TypeGenerator typeGenerator;
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
     *     users: [User]
     * }
     *
     * type Mutation{
     *     createProject(
     *         payload: ProjectInput
     *     ): Project
     *     createMilestone(
     *         payload: MilestoneInput
     *     ): Milestone
     * }
     *
     * """
     * 项目分类
     * """
     * enum ProjectCategory {
     *     """
     *     示例项目
     *     """
     *     DEMO
     *     """
     *     生产项目
     *     """
     *     PRODUCTION
     * }
     *
     * """
     * 名称
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
     *     status: MilestoneStatus
     * }
     *
     * type User {
     *     name: String!
     * }
     *
     * """
     * 里程碑状态
     * """
     * enum MilestoneStatus{
     *     """
     *     未开始
     *     """
     *     NOT_STARTED,
     *     """
     *     进行中
     *     """
     *     DOING,
     *     """
     *     发布
     *     """
     *     RELEASE,
     *     """
     *     关闭
     *     """
     *     CLOSE
     * }
     *
     * input ProjectInput{
     *     name: String!
     *     description: String!
     *     vendorBranches: [String!]!
     *     category: ProjectCategory!
     * }
     *
     *
     * input MilestoneInput{
     *     projectId: String!
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
                "    users: [User]\n" +
                "}\n" +
                "\n" +
                "type Mutation{\n" +
                "    createProject(\n" +
                "        payload: ProjectInput\n" +
                "    ): Project\n" +
                "    createMilestone(\n" +
                "        payload: MilestoneInput\n" +
                "    ): Milestone\n" +
                "}\n" +
                "\n" +
                "\"\"\"\n" +
                "项目分类\n" +
                "\"\"\"\n" +
                "enum ProjectCategory {\n" +
                "    \"\"\"\n" +
                "    示例项目\n" +
                "    \"\"\"\n" +
                "    DEMO\n" +
                "    \"\"\"\n" +
                "    生产项目\n" +
                "    \"\"\"\n" +
                "    PRODUCTION\n" +
                "}\n" +
                "\n" +
                "\"\"\"\n" +
                "名称\n" +
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
                "    status: MilestoneStatus\n" +
                "}\n" +
                "\n" +
                "type User {\n" +
                "    name: String!\n" +
                "}\n" +
                "\n" +
                "\"\"\"\n" +
                "里程碑状态\n" +
                "\"\"\"\n" +
                "enum MilestoneStatus{\n" +
                "    \"\"\"\n" +
                "    未开始\n" +
                "    \"\"\"\n" +
                "    NOT_STARTED,\n" +
                "    \"\"\"\n" +
                "    进行中\n" +
                "    \"\"\"\n" +
                "    DOING,\n" +
                "    \"\"\"\n" +
                "    发布\n" +
                "    \"\"\"\n" +
                "    RELEASE,\n" +
                "    \"\"\"\n" +
                "    关闭\n" +
                "    \"\"\"\n" +
                "    CLOSE\n" +
                "}\n" +
                "\n" +
                "input ProjectInput{\n" +
                "    name: String!\n" +
                "    description: String!\n" +
                "    vendorBranches: [String!]!\n" +
                "    category: ProjectCategory!\n" +
                "}\n" +
                "\n" +
                "\n" +
                "input MilestoneInput{\n" +
                "    projectId: String!\n" +
                "    name: String!\n" +
                "}";
        typeDefinitionRegistry = new SchemaParser().parse(schema);
        typeGenerator = new TypeGenerator(new PackageManager("a.b.c", typeDefinitionRegistry));
    }

    @Test
    public void type_gen_e2e_test() {
        String expected = "package a.b.c.type;" +
                "import a.b.c.api.ProjectService;" +
                "import a.b.c.dictionary.ProjectCategory;" +
                "import ai.care.arc.dgraph.annotation.DgraphType;" +
                "import ai.care.arc.dgraph.annotation.UidField;" +
                "import ai.care.arc.dgraph.dictionary.IDgraphType;" +
                "import ai.care.arc.graphql.annotation.DataFetcherService;" +
                "import ai.care.arc.graphql.annotation.GraphqlMethod;" +
                "import graphql.schema.DataFetcher;" +
                "import java.lang.String;" +
                "import java.time.OffsetDateTime;" +
                "import java.util.List;" +
                "import org.springframework.beans.factory.annotation.Autowired;" +
                "/**" +
                " * 名称" +
                " * 为了达到某个产品迭代、产品模块开发、或者科研调研等目的所做的工作." +
                " * Generate with GraphQL Schema By Arc" +
                " */" +
                "@DataFetcherService" +
                "@DgraphType(\"PROJECT\")" +
                "public class Project implements IDgraphType {" +
                "  /**" +
                "   * id" +
                "   */" +
                "  @UidField" +
                "  private String id;" +
                "  /**" +
                "   * name" +
                "   */" +
                "  private String name;" +
                "  /**" +
                "   * description" +
                "   */" +
                "  private String description;" +
                "  /**" +
                "   * category" +
                "   */" +
                "  private List<ProjectCategory> category;" +
                "  /**" +
                "   * createTime" +
                "   */" +
                "  private OffsetDateTime createTime;" +
                "  @Autowired" +
                "  private ProjectService projectService;" +
                "  @GraphqlMethod(" +
                "      type = \"Project\"" +
                "  )" +
                "  public DataFetcher<List<Milestone>> milestones() {" +
                "    return dataFetchingEnvironment ->  {" +
                "       return projectService.handleMilestones(dataFetchingEnvironment);" +
                "      };" +
                "    }" +
                "    public void setId(String id) {" +
                "      this.id = id;" +
                "    }" +
                "    public String getId() {" +
                "      return id;" +
                "    }" +
                "    public void setName(String name) {" +
                "      this.name = name;" +
                "    }" +
                "    public String getName() {" +
                "      return name;" +
                "    }" +
                "    public void setDescription(String description) {" +
                "      this.description = description;" +
                "    }" +
                "    public String getDescription() {" +
                "      return description;" +
                "    }" +
                "    public void setCategory(List<ProjectCategory> category) {" +
                "      this.category = category;" +
                "    }" +
                "    public List<ProjectCategory> getCategory() {" +
                "      return category;" +
                "    }" +
                "    public void setCreateTime(OffsetDateTime createTime) {" +
                "      this.createTime = createTime;" +
                "    }" +
                "    public OffsetDateTime getCreateTime() {" +
                "      return createTime;" +
                "    }" +
                "  }";
        assertEquals(typeGenerator.apply(typeDefinitionRegistry).findAny()
                .orElseThrow(NullPointerException::new)
                .toString()
                .replaceAll("\n", ""), expected);
    }

}