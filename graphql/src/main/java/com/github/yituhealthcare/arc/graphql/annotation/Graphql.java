package com.github.yituhealthcare.arc.graphql.annotation;

import com.github.yituhealthcare.arc.graphql.support.GraphqlPostProcessor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 声明DataFetcher方法并作为service加入spring生命周期
 *
 * @author wangyuheng
 * @see org.springframework.stereotype.Service
 * @see GraphqlQuery
 * @see GraphqlMutation
 * @see GraphqlPostProcessor
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface Graphql {

}
