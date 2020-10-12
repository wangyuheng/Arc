package ai.care.generatorsample.generated.type;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.graphql.annotation.DataFetcherService;
import ai.care.arc.graphql.annotation.GraphqlMethod;
import ai.care.generatorsample.generated.api.ProjectService;
import ai.care.generatorsample.generated.dictionary.ProjectCategory;
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
@DataFetcherService
@DgraphType("PROJECT")
public class Project implements IDgraphType {
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
  private ProjectService projectService;

  @GraphqlMethod(
      type = "Project"
  )
  public DataFetcher<List<Milestone>> milestones() {
    return dataFetchingEnvironment ->  {
       return projectService.handleMilestones(dataFetchingEnvironment);
      };
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
