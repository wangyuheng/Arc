package ai.care.arc.dgraph.repository;

import ai.care.arc.core.pageable.QueryBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DgraphRepository<T> {

    void createRelationship(RelationshipInformation information);

    <S extends T> String save(S entity);

    <S extends T> S saveAndReturn(S entity);

    void mutation(String path, Map<String, String> vars);

    <S extends T>  void upsert(S entity, String path, Map<String, String> vars);

    Optional<T> queryForObject(String path, Map<String, String> vars);

    List<T> queryForList(String path, Map<String, String> vars);

    List<T> queryForList(String path);

    Integer count(QueryBody queryBody);

    List<T> listAll(QueryBody queryBody);

    void deleteRelationship(String uid, String linkName, String linkUid);
}
