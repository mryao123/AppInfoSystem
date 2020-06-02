package com.jbit.servlet;

import com.jbit.entity.AppCategory;
import com.jbit.service.AppCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("dev")
public class AppCategoryServlet {

    @Resource
    private AppCategoryService appCategoryService;

    @RequestMapping("/categorylevellist")
    @ResponseBody
    public List<AppCategory> qreuyByid(Integer pid){

        return appCategoryService.qureyBypid(pid);
    }
}
