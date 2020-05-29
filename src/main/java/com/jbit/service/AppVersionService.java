package com.jbit.service;

import com.jbit.entity.AppInfo;
import com.jbit.entity.AppVersion;
import com.jbit.mapper.AppVersionMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.nio.channels.Pipe;
import java.util.List;

@Service
public class AppVersionService {
    @Resource
    private AppVersionMapper appVersionMapper;

    @Resource
    private DevInfoService devInfoService;

    @Resource
    private DevDictionaryService devDictionaryService;

    public AppVersion qureyid(Long vid){
        return appVersionMapper.selectByPrimaryKey(vid);
    }


    //查询
    public List<AppVersion> qureyidList(Long id) {
        Example example=new Example(AppVersion.class);
        example.orderBy("modifydate").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appid",id);
        List<AppVersion> select = appVersionMapper.selectByExample(example);
        bindAppName(select);
        System.out.println(select);
        return select;
    }

    private void bindAppName(List<AppVersion> select) {
        select.forEach(u->{
            u.setAppname(devInfoService.qureyByid(u.getAppid()).getSoftwarename());
            u.setPublishstatusname(devDictionaryService.qureydata("PUBLISH_STATUS",u.getPublishstatus()).getValuename());
        });
    }


    public void seve(AppVersion appVersion) {

        appVersionMapper.insertSelective(appVersion);
    }


    public void update(AppVersion appVersion) {
        appVersionMapper.updateByPrimaryKeySelective(appVersion);
    }
}
