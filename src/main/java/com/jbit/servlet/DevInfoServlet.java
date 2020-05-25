package com.jbit.servlet;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.jbit.entity.AppInfo;
import com.jbit.entity.DevUser;
import com.jbit.service.AppCategoryService;
import com.jbit.service.DevDictionaryService;
import com.jbit.service.DevInfoService;
import com.mysql.fabric.xmlrpc.base.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.awt.SunHints;
import sun.management.jmxremote.ConnectorBootstrap;
import sun.management.snmp.AdaptorBootstrap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("list")
public class DevInfoServlet {
    @Resource
    private DevInfoService devInfoService;

    //级别
    @Resource
    private AppCategoryService appCategoryService;

    //状态和平台查询
    @Resource
    private DevDictionaryService devDictionaryService;

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

        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
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
            model.addAttribute("categorylevel3list",appCategoryService.qureyBypid(queryCategoryLevel2));
        }

        //刷新不丢失值
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);

        System.out.println("qureystatus="+queryStatus);
        System.out.println(model.getAttribute("statusList"));
        return "jsp/developer/appinfolist";
    }
}
