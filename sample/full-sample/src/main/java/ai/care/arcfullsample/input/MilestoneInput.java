package ai.care.arcfullsample.input;

public class MilestoneInput {
    private String projectId;
    private String name;

    public MilestoneInput() {
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getName() {
        return this.name;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "MilestoneInput(projectId=" + this.getProjectId() + ", name=" + this.getName() + ")";
    }
}
