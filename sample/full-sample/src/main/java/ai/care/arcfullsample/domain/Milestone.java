package ai.care.arcfullsample.domain;

import ai.care.arcfullsample.dictionary.MilestoneStatus;
import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.graphql.annotation.DataFetcherService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataFetcherService
@DgraphType("MILESTONE")
@Data
@NoArgsConstructor
public class Milestone  {

    public Milestone(String name) {
        this.name = name;
    }

    @UidField
    private String id;
    private String name;

    private MilestoneStatus status;
}
