package io.github.wangyuheng.arc.graphql.interceptor;

import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 通过匹配条件判断是否在处理 {@link graphql.schema.DataFetcher} 时执行 AOP方法调用
 *
 * @author yuheng.wang
 */
public class ConditionalInterceptorWrapper implements DataFetcherHandlerInterceptor {

    private final DataFetcherHandlerInterceptor interceptor;

    private final List<String> includePatterns = new ArrayList<>();

    private final List<String> excludePatterns = new ArrayList<>();

    public ConditionalInterceptorWrapper(DataFetcherHandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public ConditionalInterceptorWrapper addPatterns(String... patterns) {
        return addPatterns(Arrays.asList(patterns));
    }

    public ConditionalInterceptorWrapper addPatterns(List<String> patterns) {
        this.includePatterns.addAll(patterns);
        return this;
    }

    public ConditionalInterceptorWrapper excludePatterns(String... patterns) {
        return excludePatterns(Arrays.asList(patterns));
    }

    public ConditionalInterceptorWrapper excludePatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    /**
     * 当前dataFetcher方法是否匹配interceptor规则
     * <p>
     * 未指定pattern时全部匹配
     */
    public boolean isMatch(String dataFetcherName) {
        Predicate<String> regexMatch = dataFetcherName::matches;
        if (excludePatterns.stream().anyMatch(regexMatch)) {
            return false;
        } else {
            if (ObjectUtils.isEmpty(this.includePatterns)) {
                return true;
            } else {
                return includePatterns.stream().anyMatch(regexMatch);
            }
        }
    }

    public DataFetcherHandlerInterceptor getInterceptor() {
        return interceptor;
    }
}
