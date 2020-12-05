package a.b.c.type;

import a.b.c.dictionary.MilestoneStatus;
import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDgraphType;
import com.github.yituhealthcare.arc.graphql.annotation.DataFetcherService;
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