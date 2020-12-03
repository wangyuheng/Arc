package ai.care.arc.graphqlclient;

import ai.care.arc.graphqlclient.annotation.GraphqlMapping;
import ai.care.arc.graphqlclient.annotation.GraphqlParam;
import ai.care.arc.graphqlclient.model.GraphqlRequest;
import org.springframework.core.io.ClassPathResource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
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
            String ql = String.join("", Files.readAllLines(new ClassPathResource(graphqlMapping.path()).getFile().toPath()));
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
            return graphqlTemplate.execute(this.url, request);
        } else {
            return method.invoke(proxy, args);
        }
    }
}