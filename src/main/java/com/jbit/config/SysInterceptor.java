package com.jbit.config;

import com.jbit.entity.DevUser;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SysInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        if(devUserSession!=null){
            return true;
        }
        //如果没有登录就返回login
        response.sendRedirect("/jsp/devlogin.jsp");
        return false;
    }
}
