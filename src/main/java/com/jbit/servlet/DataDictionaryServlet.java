package com.jbit.servlet;

import com.jbit.entity.DataDictionary;
import com.jbit.service.DevDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DataDictionaryServlet {
    @Resource
    private DevDictionaryService devDictionaryService;

    @RequestMapping("/datadictionarylist")
    public List<DataDictionary> qureyList(String tcode){

        return devDictionaryService.qureydatalist(tcode);
    }


}
