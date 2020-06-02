package com.jbit.servlet.backendservlet;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.jbit.entity.AppInfo;
import com.jbit.entity.BackendUser;
import com.jbit.entity.DevUser;
import com.jbit.service.AppCategoryService;
import com.jbit.service.AppVersionService;
import com.jbit.service.DevDictionaryService;
import com.jbit.service.DevInfoService;
import com.jbit.service.backendservice.Backend_UserService;
import com.jbit.util.MD5;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("back")
public class Backend_UserServlet {
    @Resource
    private Backend_UserService backend_userService;

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private DevInfoService devInfoService;

    @Resource
    private DevDictionaryService devDictionaryService;

    @RequestMapping("/login")
    public String backLogin(HttpSession session, Model model, BackendUser backendUser){
        if(backendUser.getUsercode()==null || backendUser.getUserpassword()==null){
            return "jsp/backendlogin";
        }
        backendUser.setUserpassword(MD5.getMD5(backendUser.getUserpassword()));
        BackendUser backendUser1 = backend_userService.backLogin(backendUser);
        if(backendUser1!=null){
            session.setAttribute("userSession",backendUser1);
            return "redirect:/jsp/backend/main.jsp";
        }else{
            model.addAttribute("error","用户名或密码错误");
        }
        return "jsp/backendlogin";

    }

    //list
    @RequestMapping("/applist")
    public String applist(HttpSession session,
                          Model model,
                          @RequestParam(defaultValue = "1",value = "pageIndex",required = true) Integer pageIndexs,
                          String querySoftwareName,//软件名称
                          Long queryStatus,//App状态
                          Long queryFlatformId,//所属品台
                          Integer queryCategoryLevel1,//级别
                          Integer queryCategoryLevel2,//级别
                          Integer queryCategoryLevel3//级别
    ){

        BackendUser devUserSession = (BackendUser) session.getAttribute("userSession");
        PageInfo<AppInfo> pageInfo = devInfoService.queryInfo(devUserSession.getId(),
                pageIndexs,querySoftwareName
                ,queryStatus,queryFlatformId,
                queryCategoryLevel1,queryCategoryLevel2,
                queryCategoryLevel3);
        model.addAttribute("appinfolist",pageInfo);
        //处理一级分类

        model.addAttribute("categoryLevel1List",appCategoryService.qureyBypid(null));
        //状态和平台

        model.addAttribute("statusList",devDictionaryService.qureydatalist("APP_STATUS"));
        model.addAttribute("flatFormList",devDictionaryService.qureydatalist("APP_FLATFORM"));
        if(queryCategoryLevel1!=null){
            model.addAttribute("categoryLevel2List",appCategoryService.qureyBypid(queryCategoryLevel1));
        }
        if(queryCategoryLevel2!=null){
            model.addAttribute("categoryLevel3list",appCategoryService.qureyBypid(queryCategoryLevel2));
        }

        //刷新不丢失值
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
        return "jsp/backend/applist";
    }

    /**
     * 审核
     */
    @RequestMapping("/check")
    public String checks(Model model,Long aid,Long vid){
        //查看app信息
        model.addAttribute("appInfo",devInfoService.qureyByid(aid));
        model.addAttribute("appVersion", appVersionService.qureyid(vid));
        System.out.println(devInfoService.qureyByid(aid));
        System.out.println( appVersionService.qureyid(vid));
        return "jsp/backend/appcheck";
    }

    //审核修改
    @RequestMapping("/checksave")
    public String checksave(AppInfo appinfo){
        devInfoService.update(appinfo);
        return "redirect:/back/applist";
    }

}
