package com.jbit.config;

import com.jbit.entity.BackendUser;
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
        BackendUser backUserSession = (BackendUser) session.getAttribute("userSession");
        if(devUserSession!=null || backUserSession!=null){
            return true;
        }
        //如果没有登录就返回login
        response.sendRedirect("/index.jsp");
        return false;
    }
}
