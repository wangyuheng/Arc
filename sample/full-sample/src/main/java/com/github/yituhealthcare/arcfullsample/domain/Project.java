package io.github.wangyuheng.arcfullsample.domain;


import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.annotation.RelationshipField;
import io.github.wangyuheng.arc.dgraph.annotation.UidField;
import io.github.wangyuheng.arc.dgraph.repository.RelationshipInformation;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMutation;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlQuery;
import io.github.wangyuheng.arc.graphql.event.DomainEvent;
import io.github.wangyuheng.arc.graphql.util.GraphqlPayloadUtil;
import io.github.wangyuheng.arc.mq.Message;
import io.github.wangyuheng.arc.mq.consumer.Consumer;
import io.github.wangyuheng.arcfullsample.dictionary.MilestoneStatus;
import io.github.wangyuheng.arcfullsample.dictionary.ProjectCategory;
import io.github.wangyuheng.arcfullsample.infrastructure.MilestoneRepository;
import io.github.wangyuheng.arcfullsample.infrastructure.ProjectRepository;
import io.github.wangyuheng.arcfullsample.input.MilestoneInput;
import io.github.wangyuheng.arcfullsample.input.ProjectInput;
import graphql.schema.DataFetcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Graphql
@DgraphType("PROJECT")
public class Project {

    private static final String RELATIONSHIP_HAS = "has";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Project.class);

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;


    @UidField
    private String id;
    private String name;
    private String description;
    private ProjectCategory category;
    private OffsetDateTime createTime;
    @RelationshipField(RELATIONSHIP_HAS)
    private List<Milestone> milestoneList;

    public Project(ProjectRepository projectRepository, MilestoneRepository milestoneRepository, String id, String name, String description, ProjectCategory category, OffsetDateTime createTime, List<Milestone> milestoneList) {
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.createTime = createTime;
        this.milestoneList = milestoneList;
    }

    public Project() {
    }

    @GraphqlMutation
    public DataFetcher<Project> createProject() {
        return dataFetchingEnvironment -> {
            ProjectInput input = GraphqlPayloadUtil.resolveArguments(dataFetchingEnvironment.getArguments(), ProjectInput.class);
            OffsetDateTime now = OffsetDateTime.now();

            this.name = input.getName();
            this.description = input.getDescription();
            this.category = input.getCategory();
            this.createTime = now;

            this.id = projectRepository.save(this);
            return this;
        };
    }

    @GraphqlMutation
    public DataFetcher<Milestone> createMilestone() {
        return dataFetchingEnvironment -> {
            MilestoneInput input = GraphqlPayloadUtil.resolveArguments(dataFetchingEnvironment.getArguments(), MilestoneInput.class);

            //可以正向通过project创建milestone，也可以先创建milestone，再关联project
//            this.id = input.getProjectId();
//            this.milestoneList = Collections.singletonList(new Milestone(input.getName()));
//            this.id = projectRepository.save(this);

            Milestone milestone = new Milestone(input.getName());
            milestone.setStatus(MilestoneStatus.NOT_STARTED);
            String milestoneId = milestoneRepository.save(milestone);
            milestone.setId(milestoneId);

            milestoneRepository.createRelationship(RelationshipInformation.builder()
                    .sourceList(Collections.singletonList(input.getProjectId()))
                    .relationship(RELATIONSHIP_HAS)
                    .targetList(Collections.singletonList(milestoneId))
                    .build());

            return milestone;
        };
    }


    @GraphqlQuery
    public DataFetcher<Project> project() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return projectRepository.getOne(id)
                    .orElse(null);
        };
    }

    @GraphqlQuery(type = "Project")
    public DataFetcher<List<Milestone>> milestones() {
        return dataFetchingEnvironment -> {
            Project project = dataFetchingEnvironment.getSource();
            List<Milestone> milestones = milestoneRepository.listByProjectId(project.id);
            String status = dataFetchingEnvironment.getArgument("status");
            if (StringUtils.isEmpty(status)) {
                return milestones;
            } else {
                MilestoneStatus milestoneStatus = MilestoneStatus.valueOf(status);
                return milestones.stream()
                        .filter(m -> m.getStatus().equals(milestoneStatus))
                        .collect(Collectors.toList());
            }
        };
    }


    @GraphqlQuery
    public DataFetcher<List<User>> users() {
        return dataFetchingEnvironment -> Arrays.asList(
                User.builder()
                        .name("u1")
                        .build(),
                User.builder()
                        .name("u2")
                        .build(),
                User.builder()
                        .name("u3")
                        .build()
        );
    }


    @Consumer(topic = "users")
    public void usersListener(Message<DomainEvent> record) {
        log.info("listen users event: {}", record);
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

    public ProjectCategory getCategory() {
        return this.category;
    }

    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    public List<Milestone> getMilestoneList() {
        return this.milestoneList;
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

    public void setCategory(ProjectCategory category) {
        this.category = category;
    }

    public void setCreateTime(OffsetDateTime createTime) {
        this.createTime = createTime;
    }

    public void setMilestoneList(List<Milestone> milestoneList) {
        this.milestoneList = milestoneList;
    }

}