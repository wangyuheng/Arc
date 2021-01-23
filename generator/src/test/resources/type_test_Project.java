package a.b.c.type;

import a.b.c.datafetcher.ProjectDataFetcher;
import a.b.c.dictionary.ProjectCategory;
import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDomainClass;
import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import com.github.yituhealthcare.arc.graphql.annotation.GraphqlMethod;
import graphql.schema.DataFetcher;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 名称
 * 为了达到某个产品迭代、产品模块开发、或者科研调研等目的所做的工作.
 * Generate with GraphQL Schema
 *
 * @author Arc
 */
@Graphql
@DgraphType("PROJECT")
public class Project implements IDomainClass {
  /**
   * id
   */
  @UidField
  private String id;

  /**
   * name
   */
  private String name;

  /**
   * description
   */
  private String description;

  /**
   * category
   */
  private List<ProjectCategory> category;

  /**
   * createTime
   */
  private OffsetDateTime createTime;

  /**
   * milestones
   */
  private List<Milestone> milestones;

  /**
   * owner
   */
  private User owner;

  /**
   * members
   */
  private List<User> members;

  /**
   * action
   */
  private String action;

  @Autowired
  private ProjectDataFetcher projectDataFetcher;

  public Project(String id, String name, String description, List<ProjectCategory> category,
                 OffsetDateTime createTime, List<Milestone> milestones, User owner, List<User> members,
                 String action, ProjectDataFetcher projectDataFetcher) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.category = category;
    this.createTime = createTime;
    this.milestones = milestones;
    this.owner = owner;
    this.members = members;
    this.action = action;
    this.projectDataFetcher = projectDataFetcher;
  }

  public Project() {
  }

  @GraphqlMethod(
          type = "Project"
  )
  public DataFetcher<List<Milestone>> milestones() {
    return dataFetchingEnvironment -> projectDataFetcher.handleMilestones(dataFetchingEnvironment);
  }

  @GraphqlMethod(
          type = "Project"
  )
  public DataFetcher<User> owner() {
    return dataFetchingEnvironment -> projectDataFetcher.handleOwner(dataFetchingEnvironment);
  }

  @GraphqlMethod(
          type = "Project"
  )
  public DataFetcher<List<User>> members() {
    return dataFetchingEnvironment -> projectDataFetcher.handleMembers(dataFetchingEnvironment);
  }

  @GraphqlMethod(
          type = "Project"
  )
  public DataFetcher<String> action() {
    return dataFetchingEnvironment -> projectDataFetcher.handleAction(dataFetchingEnvironment);
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setCategory(List<ProjectCategory> category) {
    this.category = category;
  }

  public List<ProjectCategory> getCategory() {
    return category;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setMilestones(List<Milestone> milestones) {
    this.milestones = milestones;
  }

  public List<Milestone> getMilestones() {
    return milestones;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public User getOwner() {
    return owner;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }

  public List<User> getMembers() {
    return members;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getAction() {
    return action;
  }

  public static ProjectBuilder builder() {
    return new ProjectBuilder();
  }

  public static class ProjectBuilder {
    private String id;

    private String name;

    private String description;

    private List<ProjectCategory> category;

    private OffsetDateTime createTime;

    private List<Milestone> milestones;

    private User owner;

    private List<User> members;

    private String action;

    private ProjectDataFetcher projectDataFetcher;

    private ProjectBuilder() {
    }

    public ProjectBuilder id(String id) {
      this.id = id;
      return this;
    }

    public ProjectBuilder name(String name) {
      this.name = name;
      return this;
    }

    public ProjectBuilder description(String description) {
      this.description = description;
      return this;
    }

    public ProjectBuilder category(List<ProjectCategory> category) {
      this.category = category;
      return this;
    }

    public ProjectBuilder createTime(OffsetDateTime createTime) {
      this.createTime = createTime;
      return this;
    }

    public ProjectBuilder milestones(List<Milestone> milestones) {
      this.milestones = milestones;
      return this;
    }

    public ProjectBuilder owner(User owner) {
      this.owner = owner;
      return this;
    }

    public ProjectBuilder members(List<User> members) {
      this.members = members;
      return this;
    }

    public ProjectBuilder action(String action) {
      this.action = action;
      return this;
    }

    public ProjectBuilder projectDataFetcher(ProjectDataFetcher projectDataFetcher) {
      this.projectDataFetcher = projectDataFetcher;
      return this;
    }

    public Project build() {
      return new Project(id,name,description,category,createTime,milestones,owner,members,action,projectDataFetcher);
    }
  }
}