package ai.care.arc.graphql.trace;

import brave.SpanCustomizer;
import brave.http.HttpResponse;
import brave.http.HttpResponseParser;
import brave.propagation.TraceContext;
import brave.servlet.HttpServletResponseWrapper;

import javax.servlet.http.HttpServletResponse;

/**
 * 基于Graphql"请求的方法名"进行zipkin上报
 * 在首页的trace列表中可以通过"请求的方法名"分辨请求，否则全部请求都是POST graphql
 *
 * @see ai.care.arc.graphql.rest.GraphQLController#dispatcher
 * @see org.springframework.cloud.sleuth.instrument.web.SleuthHttpServerParser
 *
 * @author yuheng.wang
 */
public class GraphqlSleuthHttpServerResponseParser implements HttpResponseParser {

    private static final String STATUS_CODE_KEY = "http.status_code";

    @Override
    public void parse(HttpResponse response, TraceContext context, SpanCustomizer span) {
        int httpStatus = response.statusCode();
        if (httpStatus == 0) {
            return; // already parsed the error
        }

        if (httpStatus == HttpServletResponse.SC_OK && response.error() != null) {
            // Filter chain threw exception but the response status may not have been set
            // yet, so we have to guess.
            span.tag(STATUS_CODE_KEY,
                    String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
        // only tag valid http statuses
        else if (httpStatus >= 100 && (httpStatus < 200) || (httpStatus > 399)) {
            span.tag(STATUS_CODE_KEY, String.valueOf(httpStatus));
        }
        // extract graphql request schema for zipkin view
        if (response instanceof HttpServletResponseWrapper) {
            Object unwrap = response.unwrap();
            if (unwrap instanceof HttpServletResponse) {
                span.name(((HttpServletResponse) unwrap).getHeader(HttpResponseParser.class.getName()));
            }
        }

    }
}