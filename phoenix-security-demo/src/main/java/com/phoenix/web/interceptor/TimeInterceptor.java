package com.phoenix.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TimeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        System.out.println("preHandle...");

        System.out.println(((HandlerMethod)o).getBean().getClass().getName());
        System.out.println(((HandlerMethod)o).getMethod().getName());

        request.setAttribute("time",new Date().getTime());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

        System.out.println("postHandle...");

        long time = (long) request.getAttribute("time");

        System.out.println("interceptor 耗时： "+ (new Date().getTime()-time));

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

        System.out.println("afterCompletion...");

        long time = (long) request.getAttribute("time");

        System.out.println("exception is "+e);

        System.out.println("interceptor 耗时： "+ (new Date().getTime()-time));
    }
}
