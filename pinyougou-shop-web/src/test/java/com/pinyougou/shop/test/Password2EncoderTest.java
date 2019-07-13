package com.pinyougou.shop.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author wangyangkun
 * @date 2019/7/13 0013 14:57
 */
public class Password2EncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String encode = bc.encode("mayun");
        System.out.println(encode);
    }
}
