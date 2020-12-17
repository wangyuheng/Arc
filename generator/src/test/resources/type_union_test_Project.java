package a.b.c.type;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDomainClass;
import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import java.lang.String;

/**
 * Project
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

  public Project(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public Project() {
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

  public static ProjectBuilder builder() {
    return new ProjectBuilder();
  }

  public static class ProjectBuilder {
    private String id;

    private String name;

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

    public Project build() {
      return new Project(id,name);
    }
  }
}