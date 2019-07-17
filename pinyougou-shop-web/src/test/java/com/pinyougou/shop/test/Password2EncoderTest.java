package com.pinyougou.shop.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @author wangyangkun
 * @date 2019/7/13 0013 14:57
 */
public class Password2EncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String encode = bc.encode("123456");
        System.out.println(encode);
    }
}
