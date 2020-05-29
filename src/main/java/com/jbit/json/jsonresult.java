package com.jbit.json;

import java.util.HashMap;

public class jsonresult extends HashMap<String,Object> {
    public jsonresult(boolean success){
        this.put("success",success);
    }
    public jsonresult(String msg){
        this.put("msg",msg);
    }

}
