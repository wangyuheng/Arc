package ai.care.arc.core.dictionary;

import java.util.Arrays;
import java.util.Optional;

/**
 * Dgraph 谓语类型
 * <p>
 * See the <a href="https://dgraph.io/docs/tutorial-3/#data-types-for-predicates">Dgraph official</a> for more details.
 *
 * @author yuheng.wang
 */
public enum DgraphPredicateTypeEnum {

    /**
     * The uid types represent predicates between two nodes.
     * In other words, they represent edges connecting two nodes.
     */
    UID("uid"),
    /**
     * Representing Geolocation data
     */
    GEO("geo"),
    DATE_TIME("dateTime"),
    STRING("string"),
    FLOAT("float"),
    INT("int"),
    BOOL("bool"),

    //scalar
    /**
     * A password for an entity is set with setting the schema for the attribute to be of type password.
     * Passwords cannot be queried directly, only checked for a match using the checkpwd function.
     * The passwords are encrypted using bcrypt.
     */
    PASSWORD("password"),
    ;

    private String key;

    DgraphPredicateTypeEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static Optional<DgraphPredicateTypeEnum> parse(String key) {
        return Arrays.stream(values()).filter(i -> i.getKey().equalsIgnoreCase(key)).findAny();
    }

    public static boolean exist(String key) {
        return Arrays.stream(values()).anyMatch(i -> i.getKey().equalsIgnoreCase(key));
    }
}
