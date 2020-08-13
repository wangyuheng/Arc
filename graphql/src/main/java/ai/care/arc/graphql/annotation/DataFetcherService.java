package ai.care.arc.graphql.annotation;

import ai.care.arc.graphql.support.DataFetcherServicePostProcessor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 声明DataFetcher方法并作为service加入spring生命周期
 *
 * @see org.springframework.stereotype.Service
 * @see GraphqlQuery
 * @see GraphqlMutation
 * @see DataFetcherServicePostProcessor
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface DataFetcherService {

}
