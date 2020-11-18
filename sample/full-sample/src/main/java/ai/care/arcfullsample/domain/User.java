package ai.care.arcfullsample.domain;

public class User {

    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", name=" + this.getName() + ")";
    }

    public static class UserBuilder {
        private String id;
        private String name;

        UserBuilder() {
        }

        public User.UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public User.UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public User build() {
            return new User(id, name);
        }

        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", name=" + this.name + ")";
        }
    }
}
