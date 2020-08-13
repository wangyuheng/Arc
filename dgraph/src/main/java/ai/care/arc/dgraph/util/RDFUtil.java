package ai.care.arc.dgraph.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 解析RDF，用于执行dgraph操作
 * <p>
 * https://www.w3.org/TR/rdf11-concepts/#section-Datatypes
 */
public class RDFUtil {

    private static final String DELIMITER = " ";

    private RDFUtil() {
    }

    public static String wrapper(String str) {
        return "<" + str + ">";
    }

    public static String wrapperAndJoin(String... items) {
        return Arrays.stream(items)
                .map(RDFUtil::wrapper)
                .collect(Collectors.joining(DELIMITER)) + " . ";
    }

}
