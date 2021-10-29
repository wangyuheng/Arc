package io.github.wangyuheng.arc.graphqlclient.model;

import java.util.List;
import java.util.Map;

/**
 * The interface describing graphql errors
 *
 * @author yuheng.wang
 * @see <a href="https://facebook.github.io/graphql/#sec-Errors">GraphQL Spec - 7.2.2 Errors</a>
 */
public class GraphqlError {

    /**
     * a description of the error intended for the developer as a guide to understand and correct the error
     */
    private String message;
    /**
     * The graphql spec says that the (optional) path field of any error should be a list
     * of path entries - http://facebook.github.io/graphql/#sec-Errors
     * <p>
     * the path in list format
     */
    private String path;

    /**
     * a map of error extensions or null if there are none
     */
    private Map<String, Object> extensions;

    /**
     * the location(s) within the GraphQL document at which the error occurred. Each {@link SourceLocation}
     * describes the beginning of an associated syntax element
     */
    private List<SourceLocation> locations;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public List<SourceLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<SourceLocation> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "GraphqlError{" +
                "message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", extensions=" + extensions +
                ", locations=" + locations +
                '}';
    }
}
