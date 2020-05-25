package com.jbit.service;

import com.jbit.entity.DataDictionary;
import com.jbit.mapper.DataDictionaryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DevDictionaryService {
    @Resource
    private DataDictionaryMapper dataDictionaryMapper;

    public DataDictionary qureydata(String typecode,Long valueid){
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypecode(typecode);
        dataDictionary.setValueid(valueid);
        return dataDictionaryMapper.selectOne(dataDictionary);
    }

    //查询状态和平台
    public List<DataDictionary> qureydatalist(String typecode){
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypecode(typecode);
        return dataDictionaryMapper.select(dataDictionary);
    }
}
