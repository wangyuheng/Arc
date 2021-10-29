package a.b.c.type;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.dictionary.IDomainClass;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import java.lang.String;

/**
 * User
 * Generate with GraphQL Schema
 *
 * @author Arc
 */
@Graphql
@DgraphType("USER")
public class User implements IDomainClass {
  /**
   * name
   */
  private String name;

  public User(String name) {
    this.name = name;
  }

  public User() {
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

  public static class UserBuilder {
    private String name;

    private UserBuilder() {
    }

    public UserBuilder name(String name) {
      this.name = name;
      return this;
    }

    public User build() {
      return new User(name);
    }
  }
}