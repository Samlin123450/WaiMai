package com.sam.Reggie.filter;

import com.alibaba.fastjson.JSON;
import com.sam.Reggie.common.R;
import com.sam.Reggie.common.threadValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class loginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截url：{}", request.getRequestURI());

        String url = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg",
                "/common/**"
        };
        //判断url是否属于可以直接放行的
        boolean check = check(url, urls);
        if(check){
            log.info("本次{}请求不需要处理",url);
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getSession().getAttribute("employeeId")!=null){
            log.info("本次{}请求已成功登录,用户id为{}",url,request.getSession().getAttribute("employeeId"));
            long id = (long)request.getSession().getAttribute("employeeId");

            threadValue.setId(id);
            filterChain.doFilter(request, response);
            return;
        }else {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        }

//        if(request.getSession().getAttribute("userId")!=null){
//            log.info("本次{}请求已成功登录,用户id为{}",url,request.getSession().getAttribute("userId"));
//            long id = (long)request.getSession().getAttribute("userId");
//
//            threadValue.setId(id);
//            filterChain.doFilter(request, response);
//            return;
//        }

    }
    public static boolean check(String url,String[] urls){
        for(String url1:urls){
            if(PATH_MATCHER.match(url1,url)){
                return true;
            }
        }
        return false;
    }
}
