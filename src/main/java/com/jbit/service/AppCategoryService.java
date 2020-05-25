package com.jbit.service;

import com.jbit.entity.AppCategory;
import com.jbit.mapper.AppCategoryMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppCategoryService {
    @Resource
    private AppCategoryMapper appCategoryMapper;

    public AppCategory qureyid(Long cid){
        return appCategoryMapper.selectByPrimaryKey(cid);
    }

    //级别查询
    public List<AppCategory> qureyBypid(Integer id){
        Example example=new Example(AppCategory.class);
        Example.Criteria criteria = example.createCriteria();

        if(id==null){
            //查询一级
            criteria.andIsNull("parentid");

        }else{
            //查询二级和三级
            criteria.andEqualTo("parentid",id);
        }
        return appCategoryMapper.selectByExample(example);
    }

}
