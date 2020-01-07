package com.villas.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //请求被路由之前执行
        return "pre";
    }

    @Override
    public int filterOrder() {
        //过滤器执行的顺序
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //该过滤器是否要执行
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        log.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        String token = request.getParameter("token");
        String headerToken = request.getHeader("token");
        if (token == null && headerToken == null) {
            log.warn("access token is empty");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(401);
            return null;
        }
        log.info("access token is {}, or header token is {}", token, headerToken);
        return null;
    }
}
