package com.jbit.util;

import org.springframework.util.DigestUtils;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String plainText) {
        if(plainText==null){
            return null;
        }
        byte[] digest=null;
        //spring自带md5加密
        return DigestUtils.md5DigestAsHex(plainText.getBytes());

        //javaMD5加密
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");//获取MD5实例
//            digest = md.digest(plainText.getBytes("utf-8"));//此处得到的是md5加密后的byte类型值
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        //16是16进制
//
//        String md5str=new BigInteger(1,digest).toString(16);
//        return md5str;
    }

    public static void main(String[] args){
        System.out.println(getMD5("admin"));
    }
}

