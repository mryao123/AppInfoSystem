package com.jbit.service.backendservice;

import com.jbit.entity.BackendUser;
import com.jbit.mapper.BackendUserMapper;
import com.jbit.service.DevDictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class Backend_UserService {
    @Resource
    private BackendUserMapper backendUserMapper;

    @Resource
    private DevDictionaryService devDictionaryService;

    //登录
    public BackendUser backLogin(BackendUser backendUser){
        BackendUser backendUser1 = backendUserMapper.selectOne(backendUser);
        bindingName(backendUser1);
        return backendUser1;
    }

    //绑定
    private void bindingName(BackendUser backendUser1) {
        backendUser1.setUsertypename(devDictionaryService.qureydata("USER_TYPE",backendUser1.getUsertype()).getValuename());

    }





}
