package com.phoenix.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//@Component
public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Time filter inited");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Time filter start");

        long time = new Date().getTime();
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Time filter 耗时: "+(new Date().getTime()-time));

        System.out.println("Time filter finish");
    }

    @Override
    public void destroy() {
        System.out.println("Time filter destroyed");
    }
}
