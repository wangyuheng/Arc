package ai.care.arcfullsample.infrastructure;

import ai.care.arcfullsample.domain.Project;
import ai.care.arc.dgraph.repository.SimpleDgraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends SimpleDgraphRepository<Project> {

}
