package ai.care.arcgraphqlsample.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Milestone  {

    private String id;
    private String name;
    private String description;
    private MilestoneStatus status;

    enum MilestoneStatus{
        NOT_STARTED,
        DOING,
        RELEASE,
        CLOSE
    }
}
