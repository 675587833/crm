package com.bjpowernode.crm.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"*.do","*.jsp"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse){
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            String path = request.getServletPath();
            if (path.contains("login")){
                chain.doFilter(req,resp);
            }else{
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("user") == null){
                    response.sendRedirect(request.getContextPath()+"/login.jsp");
                }else {
                    chain.doFilter(req,resp);
                }

            }
        }
    }
}
