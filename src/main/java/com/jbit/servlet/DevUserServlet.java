package com.jbit.servlet;

import com.jbit.entity.DevUser;
import com.jbit.service.DevUserService;
import com.jbit.util.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("dev")
public class DevUserServlet {
    @Resource
    private DevUserService devUserService;

    @RequestMapping("/login")
    public String login(HttpSession session, Model model, DevUser devUser){
        if(devUser.getDevcode()==null){
            return "jsp/devlogin";
        }
        devUser.setDevpassword(MD5.getMD5(devUser.getDevpassword()));
        DevUser devUser1 = devUserService.queryLogin(devUser);
        if(devUser1!=null){
            session.setAttribute("devUserSession",devUser1);
            return "redirect:/jsp/developer/main.jsp";
        }else{
            model.addAttribute("error","用户名或密码错误");
        }
        return "jsp/devlogin";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/jsp/devlogin.jsp";
    }


}
