package ai.care.arcfullsample.domain;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.graphql.annotation.DataFetcherService;
import ai.care.arcfullsample.dictionary.MilestoneStatus;

@DataFetcherService
@DgraphType("MILESTONE")
public class Milestone  {

    public Milestone(String name) {
        this.name = name;
    }

    @UidField
    private String id;
    private String name;

    private MilestoneStatus status;

    public Milestone() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
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

    public void setStatus(MilestoneStatus status) {
        this.status = status;
    }

    public String toString() {
        return "Milestone(id=" + this.getId() + ", name=" + this.getName() + ", status=" + this.getStatus() + ")";
    }
}
