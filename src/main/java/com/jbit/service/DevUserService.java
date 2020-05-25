package com.jbit.service;

import com.jbit.entity.DevUser;
import com.jbit.mapper.DevUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class DevUserService {
    @Resource
    private DevUserMapper devUserMapper;

    public DevUser queryLogin(DevUser devUser){
       return devUserMapper.selectOne(devUser);

    }
}
