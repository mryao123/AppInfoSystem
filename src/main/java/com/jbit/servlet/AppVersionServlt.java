package com.jbit.servlet;

import com.jbit.entity.AppInfo;
import com.jbit.entity.AppVersion;
import com.jbit.entity.DevUser;
import com.jbit.json.jsonresult;
import com.jbit.service.AppVersionService;
import com.jbit.service.DevInfoService;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class AppVersionServlt {
    @Resource
    private AppVersionService appVersionService;

    @Resource
    private DevInfoService devInfoService;

    //查询
    @RequestMapping("appversionadd/{id}")
    public String appversionadd(Model model, @PathVariable Long id){
        List<AppVersion> appVersions = appVersionService.qureyidList(id);
        model.addAttribute("appid",id);
        model.addAttribute("appVersions",appVersions);
        return "jsp/developer/appversionadd";
    }

    //添加
    @RequestMapping("/addversionsave")
    public String addversionsave(Model model,HttpSession session, AppVersion appVersion, MultipartFile a_downloadlink ){
        //实现文件上传 到tomcat位置
        String server_path=session.getServletContext().getRealPath("statics/uploadfiles");
        try {
            a_downloadlink.transferTo(new File(server_path,a_downloadlink.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appVersion.setDownloadlink("statics/uploadfiles/"+a_downloadlink.getOriginalFilename());
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        appVersion.setCreatedby(devUserSession.getId());
        appVersion.setModifydate(new Date());
        appVersion.setCreationdate(new Date());
        appVersion.setApklocpath(server_path+a_downloadlink.getOriginalFilename()); //绝对路径
        appVersion.setApkfilename(a_downloadlink.getOriginalFilename());
        appVersionService.seve(appVersion);
        //更新版本号
        AppInfo appInfo = new AppInfo();
        appInfo.setId(appVersion.getAppid());
        appInfo.setVersionid(appVersion.getId());
        devInfoService.update(appInfo);
        return "redirect:appversionadd/"+appVersion.getAppid();
    }

    @RequestMapping("/appversionmodifys")
    public String appversionmodify(Model model,Long vid,Long aid){
        List<AppVersion> appVersions = appVersionService.qureyidList(aid);
        model.addAttribute("appid",aid);
        model.addAttribute("appVersionList",appVersions);
        model.addAttribute("appVersion",appVersionService.qureyid(vid));
        return "jsp/developer/appversionmodify";
    }

    //修改
    @RequestMapping("/appversionmodifysave")
    public String appversionmodifysaves(HttpSession session,AppVersion appVersion,MultipartFile attach){
        if(!attach.isEmpty()){
            String server_path=session.getServletContext().getRealPath("statics/uploadfiles");
            try {
                attach.transferTo(new File(server_path,attach.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            appVersion.setApklocpath(server_path+attach.getOriginalFilename()); //绝对路径
            appVersion.setApkfilename(attach.getOriginalFilename());
        }
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        appVersion.setModifydate(new Date());
        appVersion.setModifyby(devUserSession.getId());

        appVersionService.update(appVersion);
        return "redirect:/list/applist";
    }

    //删除文件
    @RequestMapping("/delfile")
    @ResponseBody
    public jsonresult delfiles(Long id,String apk){
        AppVersion qureyids = appVersionService.qureyid(id);
        try{
            File file=new File(qureyids.getDownloadlink());
            file.delete();
            qureyids.setDownloadlink("");
            qureyids.setApklocpath("");
            appVersionService.update(qureyids);
            return new jsonresult(true);
        }catch (Exception e){
            return new jsonresult(false);
        }
    }

}
