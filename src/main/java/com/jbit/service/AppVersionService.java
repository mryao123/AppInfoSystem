package com.jbit.service;

import com.jbit.entity.AppVersion;
import com.jbit.mapper.AppVersionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AppVersionService {
    @Resource
    private AppVersionMapper appVersionMapper;

    public AppVersion qureyid(Long vid){
        return appVersionMapper.selectByPrimaryKey(vid);
    }

}
