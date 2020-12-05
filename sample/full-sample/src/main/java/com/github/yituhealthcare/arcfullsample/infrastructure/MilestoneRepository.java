package com.github.yituhealthcare.arcfullsample.infrastructure;

import com.github.yituhealthcare.arc.dgraph.repository.SimpleDgraphRepository;
import com.github.yituhealthcare.arcfullsample.domain.Milestone;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MilestoneRepository extends SimpleDgraphRepository<Milestone> {

    public List<Milestone> listByProjectId(String projectId) {
        Map<String, String> vars = new HashMap<>();
        vars.put("projectId", projectId);
        return this.queryForList("milestone.listByProjectId", vars);
    }

}
