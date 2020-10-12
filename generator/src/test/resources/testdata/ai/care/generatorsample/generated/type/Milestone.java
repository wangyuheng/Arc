package ai.care.generatorsample.generated.type;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.graphql.annotation.DataFetcherService;
import ai.care.generatorsample.generated.dictionary.MilestoneStatus;
import java.lang.String;

/**
 * 里程碑
 * 表述一个Project的某个时间阶段及阶段性目标. 一个Project可以同时拥有多个处于相同或者不同阶段的Milestone.
 * Generate with GraphQL Schema By Arc
 */
@DataFetcherService
@DgraphType("MILESTONE")
public class Milestone implements IDgraphType {
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
   * status
   */
  private MilestoneStatus status;

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

  public void setStatus(MilestoneStatus status) {
    this.status = status;
  }

  public MilestoneStatus getStatus() {
    return status;
  }
}
