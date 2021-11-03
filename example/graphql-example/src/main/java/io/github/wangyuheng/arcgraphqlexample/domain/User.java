package io.github.wangyuheng.arcgraphqlexample.domain;

public class User {

    private String id;
    private String name;
    private String projectName;

    public User(String id, String name, String projectName) {
        this.id = id;
        this.name = name;
        this.projectName = projectName;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }

    public static class UserBuilder {
        private String id;
        private String name;
        private String projectName;

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
        public User.UserBuilder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public User build() {
            return new User(id, name, projectName);
        }

        @Override
        public String toString() {
            return "UserBuilder{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", projectName='" + projectName + '\'' +
                    '}';
        }
    }
}
