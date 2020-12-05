package com.github.yituhealthcare.arcfullsample.infrastructure;

import com.github.yituhealthcare.arc.dgraph.repository.SimpleDgraphRepository;
import com.github.yituhealthcare.arcfullsample.domain.Project;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends SimpleDgraphRepository<Project> {

}
