package io.github.wangyuheng.arc.graphqlclient;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * GraphqlClient实例工厂类
 * 需要设置field的setter方法用于 {@link org.springframework.beans.factory.support.BeanDefinitionBuilder#addPropertyValue(String, Object)} 调用
 *
 * @author yuheng.wang
 */
public class GraphqlClientFactoryBean implements FactoryBean<Object> {

    private Class<?> type;
    private String url;
    private GraphqlTemplate graphqlTemplate;

    /**
     * 代理 {@link io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlClient} 的spring实例
     *
     * @return GraphqlClient的代理类Spring实例
     */
    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(this.type.getClassLoader(), new Class<?>[]{this.type}, new GraphqlClientInvocationHandler(this.url, graphqlTemplate));
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setGraphqlTemplate(GraphqlTemplate graphqlTemplate) {
        this.graphqlTemplate = graphqlTemplate;
    }
}
