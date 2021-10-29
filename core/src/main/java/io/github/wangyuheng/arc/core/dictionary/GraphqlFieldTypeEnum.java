package io.github.wangyuheng.arc.core.dictionary;

import java.util.Arrays;
import java.util.Optional;

/**
 * GraphQL 字段类型
 * 包含扩展类型 @link graphql.scalars.ExtendedScalars
 * type及枚举类型特殊标识，方便转换处理
 * - {@link GraphqlFieldTypeEnum#ENUM}
 * - {@link GraphqlFieldTypeEnum#TYPE}
 * <p>
 * See the <a href="https://graphql.org/learn/schema/#object-types-and-fields">GraphQL official</a> for more details.
 *
 * @author yuheng.wang
 */
public enum GraphqlFieldTypeEnum {

    /**
     * 枚举
     */
    ENUM("enum"),
    /**
     * Graphql type
     */
    TYPE("type"),
    /**
     * A signed 32‐bit integer.
     */
    INT("Int"),
    /**
     * A signed double-precision floating-point value.
     */
    FLOAT("Float"),
    /**
     * A UTF‐8 character sequence.
     */
    STRING("String"),
    /**
     * true or false.
     */
    BOOLEAN("Boolean"),
    /**
     * The ID scalar type represents a unique identifier,
     * often used to refetch an object or as the key for a cache.
     * The ID type is serialized in the same way as a String;
     * however, defining it as an ID signifies that it is not intended to be human‐readable.
     */
    ID("ID"),

    //scalar
    /**
     * An RFC-3339 compliant date time scalar that accepts string values like `1996-12-19T16:39:57-08:00` and produces
     * `java.time.OffsetDateTime` objects at runtime.
     *
     * @link graphql.scalars.ExtendedScalars#DateTime
     */
    DATE_TIME("DateTime"),
    /**
     * An RFC-3339 compliant date scalar that accepts string values like `1996-12-19` and produces
     * `java.time.LocalDate` objects at runtime.
     *
     * @link graphql.scalars.ExtendedScalars#Date
     */
    DATE("Date"),
    /**
     * An RFC-3339 compliant time scalar that accepts string values like `6:39:57-08:00` and produces
     * `java.time.OffsetTime` objects at runtime.
     *
     * @link graphql.scalars.ExtendedScalars#Time
     */
    TIME("Time"),
    /**
     * An object scalar allows you to have a multi level data value without defining it in the graphql schema.
     *
     * @link graphql.scalars.ExtendedScalars#Object
     */
    OBJECT("Object"),
    /**
     * A synonym class for the {@link #Object} scalar, since some people prefer their SDL to look like the following :
     *
     * @link graphql.scalars.ExtendedScalars#Json
     */
    JSON("Json"),
    /**
     * A URL scalar that accepts URL strings and produces {@link java.net.URL} objects at runtime
     *
     * @link graphql.scalars.ExtendedScalars#Url
     */
    URL("Url"),
    /**
     * A Locale scalar that accepts a IETF BCP 47 language tag string and produces {@link java.util.Locale} objects at runtime.
     *
     * @link graphql.scalars.ExtendedScalars#Locale
     */
    LOCALE("Locale"),
    /**
     * An `Int` scalar that MUST be greater than zero
     *
     * @link graphql.scalars.ExtendedScalars#PositiveInt
     */
    POSITIVE_INT("PositiveInt"),
    /**
     * An `Int` scalar that MUST be less than zero
     *
     * @link graphql.scalars.ExtendedScalars#NegativeInt
     */
    NEGATIVE_INT("NegativeInt"),
    /**
     * An `Int` scalar that MUST be less than or equal to zero
     *
     * @link graphql.scalars.ExtendedScalars#NonPositiveInt
     */
    NON_POSITIVE_INT("NonPositiveInt"),
    /**
     * An `Int` scalar that MUST be greater than or equal to zero
     *
     * @link graphql.scalars.ExtendedScalars#NonNegativeInt
     */
    NON_NEGATIVE_INT("NonNegativeInt"),
    /**
     * An `Float` scalar that MUST be greater than zero
     *
     * @link graphql.scalars.ExtendedScalars#PositiveFloat
     */
    POSITIVE_FLOAT("PositiveFloat"),
    /**
     * An `Float` scalar that MUST be less than zero
     *
     * @link graphql.scalars.ExtendedScalars#NegativeFloat
     */
    NEGATIVE_FLOAT("NegativeFloat"),
    /**
     * An `Float` scalar that MUST be less than or equal to zero
     *
     * @link graphql.scalars.ExtendedScalars#NonPositiveFloat
     */
    NON_POSITIVE_FLOAT("NonPositiveFloat"),
    /**
     * An `Float` scalar that MUST be greater than or equal to zero
     *
     * @link graphql.scalars.ExtendedScalars#NonNegativeFloat
     */
    NON_NEGATIVE_FLOAT("NonNegativeFloat");

    private String key;

    GraphqlFieldTypeEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static Optional<GraphqlFieldTypeEnum> parse(String key) {
        return Arrays.stream(values()).filter(i -> i.getKey().equalsIgnoreCase(key)).findAny();
    }

    public static boolean exist(String key) {
        return Arrays.stream(values()).anyMatch(i -> i.getKey().equalsIgnoreCase(key));
    }
}
