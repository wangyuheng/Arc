package a.b.c.datafetcher;

import a.b.c.type.Project;
import graphql.schema.DataFetchingEnvironment;

/**
 * Generate with GraphQL Schema
 *
 * @author Arc
 */
public interface QueryDataFetcher {
    Project handleProject(DataFetchingEnvironment dataFetchingEnvironment);
}