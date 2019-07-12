package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/12 0012 20:05
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name.do")
    public Map name(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        Map map = new HashMap<>();
        map.put("loginName",name);
        return map;
    }

}
