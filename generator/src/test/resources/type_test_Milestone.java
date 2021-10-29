package a.b.c.type;

import a.b.c.dictionary.MilestoneStatus;
import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.annotation.UidField;
import io.github.wangyuheng.arc.dgraph.dictionary.IDomainClass;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import java.lang.String;

/**
 * 里程碑
 * 表述一个Project的某个时间阶段及阶段性目标. 一个Project可以同时拥有多个处于相同或者不同阶段的Milestone.
 * Generate with GraphQL Schema
 *
 * @author Arc
 */
@Graphql
@DgraphType("MILESTONE")
public class Milestone implements IDomainClass {
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

  public Milestone(String id, String name, MilestoneStatus status) {
    this.id = id;
    this.name = name;
    this.status = status;
  }

  public Milestone() {
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

  public void setStatus(MilestoneStatus status) {
    this.status = status;
  }

  public MilestoneStatus getStatus() {
    return status;
  }

  public static MilestoneBuilder builder() {
    return new MilestoneBuilder();
  }

  public static class MilestoneBuilder {
    private String id;

    private String name;

    private MilestoneStatus status;

    private MilestoneBuilder() {
    }

    public MilestoneBuilder id(String id) {
      this.id = id;
      return this;
    }

    public MilestoneBuilder name(String name) {
      this.name = name;
      return this;
    }

    public MilestoneBuilder status(MilestoneStatus status) {
      this.status = status;
      return this;
    }

    public Milestone build() {
      return new Milestone(id,name,status);
    }
  }
}