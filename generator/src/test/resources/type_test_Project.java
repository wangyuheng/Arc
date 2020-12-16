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
 * Generate with GraphQL Schema By Arc
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

  @Autowired
  private ProjectDataFetcher projectDataFetcher;

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
}