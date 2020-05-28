package com.jbit.servlet;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.jbit.entity.AppInfo;
import com.jbit.entity.DevUser;
import com.jbit.json.jsonresult;
import com.jbit.service.AppCategoryService;
import com.jbit.service.DevDictionaryService;
import com.jbit.service.DevInfoService;
import com.mysql.fabric.xmlrpc.base.Data;
import com.mysql.fabric.xmlrpc.base.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.SunHints;
import sun.management.jmxremote.ConnectorBootstrap;
import sun.management.snmp.AdaptorBootstrap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;


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

    //删除图片
    @RequestMapping("/delfile")
    @ResponseBody
    public jsonresult delfile(Long id,String flag){
        AppInfo appInfo = devInfoService.qureyByid(id);
        if(flag.equals("logo")){
            try {
                //通过绝对路径删除图片
                File file=new File(appInfo.getLogolocpath());
                file.delete();
                //清空数据库里面的数据
                appInfo.setLogolocpath("");
                appInfo.setLogopicpath("");
                devInfoService.update(appInfo);
                return new jsonresult(true);
            } catch (Exception e) {
                return new jsonresult(false);
            }
        }
        return new jsonresult(false);
    }

    //修改查询
    @RequestMapping("/appinfomodify/{id}")
    public String ModifyT(Model model,@PathVariable(name = "id")Long id){
        model.addAttribute("appInfo",devInfoService.qureyByid(id));
        System.out.println(devInfoService.qureyByid(id));
        return "jsp/developer/appinfomodify";
    }

//    修改
@RequestMapping("/appinfomodify")
public String appinfomodify(HttpSession session,AppInfo appInfo, MultipartFile attach){

   if(!attach.isEmpty()){
       System.out.println("empty");
       //1实现文件上传     服务器图片存放位置
       String server_path=session.getServletContext().getRealPath("/statics/uploadfiles");
       //省略图片大小和图片规格        文件路径和文件名
       try {
           attach.transferTo(new File(server_path,attach.getOriginalFilename()));
           appInfo.setLogolocpath(server_path+attach.getOriginalFilename()); //绝对路径
           appInfo.setLogopicpath("/statics/uploadfiles/"+attach.getOriginalFilename());//相对路径
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
    //app修改
    DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
    appInfo.setDevid(devUserSession.getId());
    appInfo.setModifyby(appInfo.getId());
    appInfo.setModifydate(new Date());
    devInfoService.update(appInfo);
    return "redirect:/list/applist";

}

    //验证apkNameexist
    @RequestMapping("/apkexist")
    @ResponseBody
    public jsonresult apkexist(String APKName){
        AppInfo apkexist = devInfoService.apkexist(APKName);
        if(apkexist!=null){
            return new jsonresult(true);
        }else{
            return new jsonresult(false);
        }
    }

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

        return "jsp/developer/appinfolist";
    }

    //文件上传和添加
    @RequestMapping("/appinfoadd")
    public String appinfoadd(HttpSession session,AppInfo appInfo, MultipartFile uploadFile){
        //1实现文件上传     服务器图片存放位置
        String server_path=session.getServletContext().getRealPath("/statics/uploadfiles");
        System.out.println(uploadFile);
        System.out.println(server_path);
        //省略图片大小和图片规格        文件路径和文件名
        try {
            uploadFile.transferTo(new File(server_path,uploadFile.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //app添加
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        appInfo.setDevid(devUserSession.getId());
        appInfo.setCreatedby(appInfo.getId());
        appInfo.setCreationdate(new Date());
        appInfo.setLogolocpath(server_path+"/"+uploadFile.getOriginalFilename()); //绝对路径
        appInfo.setLogopicpath("/statics/uploadfiles/"+uploadFile.getOriginalFilename());//相对路径
        System.out.println(appInfo);
        devInfoService.seve(appInfo);

        return "redirect:/list/applist";

    }


}
