package io.github.wangyuheng.arcfullsample.domain;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.annotation.UidField;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arcfullsample.dictionary.MilestoneStatus;

@Graphql
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
