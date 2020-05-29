package com.jbit.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbit.entity.AppInfo;
import com.jbit.mapper.AppInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DevInfoService {
    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private DevDictionaryService devDictionaryService;

    @Resource
    private AppVersionService appVersionService;

    //验证apkName是否重复
    public AppInfo apkexist(String akpName){
        AppInfo appInfo=new AppInfo();
        appInfo.setApkname(akpName);
        return appInfoMapper.selectOne(appInfo);
    }


    public PageInfo<AppInfo> queryInfo(Long id, Integer pageIndexs, String querySoftwareName, Long queryStatus, Long devid, Integer queryCategoryLevel1, Integer queryCategoryLevel2, Integer queryCategoryLevel3){
        Example example=new Example(AppInfo.class);
        Example.Criteria criteria = example.createCriteria();
        PageHelper.startPage(pageIndexs,5);
        if(querySoftwareName!=null){
            criteria.andLike("softwarename","%"+querySoftwareName+"%");
        }
        if(queryStatus!=null && queryStatus!=0){
            criteria.andEqualTo("status",queryStatus);
        }
        if(devid!=null){
            criteria.andEqualTo("devid",devid);
        }
        if(queryCategoryLevel1!=null && queryCategoryLevel1!=0){
            criteria.andEqualTo("categorylevel1",queryCategoryLevel1);
        }
        if(queryCategoryLevel2!=null && queryCategoryLevel2!=0){
            criteria.andEqualTo("categorylevel2",queryCategoryLevel2);
        }
        if(queryCategoryLevel3!=null && queryCategoryLevel3!=0){
            criteria.andEqualTo("categorylevel3",queryCategoryLevel3);
        }
        List<AppInfo> appInfo = appInfoMapper.selectByExample(example);
        PageInfo<AppInfo> pageInfo=new PageInfo<>(appInfo);
        bindData(pageInfo.getList());
        return pageInfo;
    }

    /**
     * 绑定数据
     * @param appInfo
     */
    private void bindData(List<AppInfo> appInfo) {
        appInfo.forEach(app->{
            app.setFlatformname(devDictionaryService.qureydata("APP_FLATFORM",app.getFlatformid()).getValuename());
            app.setCategorylevel1name(appCategoryService.qureyid(app.getCategorylevel1()).getCategoryname());
            app.setCategorylevel2name(appCategoryService.qureyid(app.getCategorylevel2()).getCategoryname());
            app.setCategorylevel3name(appCategoryService.qureyid(app.getCategorylevel3()).getCategoryname());
            app.setFlatformname(devDictionaryService.qureydata("APP_STATUS",app.getStatus()).getValuename());
//
           if(app.getVersionid()!=null){
               app.setVersionno(appVersionService.qureyid(app.getVersionid()).getVersionno());
           }else{
               app.setVersionno(null);
           }
        });

    }

    @Transactional
    public void seve(AppInfo appInfo) {
        appInfoMapper.insertSelective(appInfo);
    }

    public AppInfo qureyByid(Long id) {
        //处理转台名字
        AppInfo appInfo = appInfoMapper.selectByPrimaryKey(id);
        appInfo.setStatusname(devDictionaryService.qureydata("APP_STATUS",appInfo.getStatus()).getValuename());
        return appInfo;
    }
    public AppInfo qureyByidKey(Long id){
        return appInfoMapper.selectByPrimaryKey(id);
    }

    public void update(AppInfo appInfo) {
        appInfoMapper.updateByPrimaryKeySelective(appInfo);
    }

    public int InfoDel(Long id) {
        int i = appInfoMapper.deleteByPrimaryKey(id);
        return i;
    }
}
