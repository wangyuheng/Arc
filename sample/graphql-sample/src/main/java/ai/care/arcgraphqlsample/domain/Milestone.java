package ai.care.arcgraphqlsample.domain;

public class Milestone  {

    private String id;
    private String name;
    private String description;
    private MilestoneStatus status;

    public Milestone(String id, String name, String description, MilestoneStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Milestone() {
    }

    public static MilestoneBuilder builder() {
        return new MilestoneBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public MilestoneStatus getStatus() {
        return this.status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(MilestoneStatus status) {
        this.status = status;
    }

    public String toString() {
        return "Milestone(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", status=" + this.getStatus() + ")";
    }

    enum MilestoneStatus{
        NOT_STARTED,
        DOING,
        RELEASE,
        CLOSE
    }

    public static class MilestoneBuilder {
        private String id;
        private String name;
        private String description;
        private MilestoneStatus status;

        MilestoneBuilder() {
        }

        public Milestone.MilestoneBuilder id(String id) {
            this.id = id;
            return this;
        }

        public Milestone.MilestoneBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Milestone.MilestoneBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Milestone.MilestoneBuilder status(MilestoneStatus status) {
            this.status = status;
            return this;
        }

        public Milestone build() {
            return new Milestone(id, name, description, status);
        }

        public String toString() {
            return "Milestone.MilestoneBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", status=" + this.status + ")";
        }
    }
}
