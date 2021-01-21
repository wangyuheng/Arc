package com.github.yituhealthcare.arc.graphqlclient;

import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlMapping;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlParam;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlRequest;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * GraphqlClient实例代理处理方法.
 * 根据Annotation解析graphql语句及参数并通过 {@link GraphqlTemplate} 进行网络请求调用.
 *
 * @author yuheng.wang
 */
class GraphqlClientInvocationHandler implements InvocationHandler {

    private final String url;
    private final GraphqlTemplate graphqlTemplate;

    public GraphqlClientInvocationHandler(String url, GraphqlTemplate graphqlTemplate) {
        this.url = url;
        this.graphqlTemplate = graphqlTemplate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(GraphqlMapping.class)) {
            GraphqlMapping graphqlMapping = method.getAnnotation(GraphqlMapping.class);
            String ql = FileCopyUtils.copyToString(new InputStreamReader(new ClassPathResource(graphqlMapping.path()).getInputStream()));
            Parameter[] parameters = method.getParameters();
            Map<String, Object> vars = new HashMap<>(parameters.length);
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(GraphqlParam.class)) {
                    vars.put(parameters[i].getAnnotation(GraphqlParam.class).value(), args[i]);
                } else {
                    vars.put(parameters[i].getName(), args[i]);
                }
            }
            GraphqlRequest request = new GraphqlRequest(ql, vars);

            Type returnType = method.getGenericReturnType();
            if (returnType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) returnType;
                Assert.isAssignable(GraphqlResponse.class, (Class<?>) pType.getRawType(), "GraphqlClient method must return GraphqlResponse!");
                Type[] actualTypeArguments = pType.getActualTypeArguments();
                Type genericType = actualTypeArguments[0];
                if (genericType instanceof ParameterizedType) {
                    throw new IllegalArgumentException("GraphqlResponse generic must be not nested!");
                } else {
                    return graphqlTemplate.execute(this.url, request, (Class<?>) actualTypeArguments[0]);
                }
            } else {
                Assert.isAssignable(GraphqlResponse.class, (Class<?>) returnType, "GraphqlClient method must return GraphqlResponse!");
                return graphqlTemplate.execute(this.url, request);
            }
        } else {
            return method.invoke(proxy, args);
        }
    }
}