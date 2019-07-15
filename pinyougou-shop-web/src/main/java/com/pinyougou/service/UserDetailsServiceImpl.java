package com.pinyougou.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证类
 * @author wangyangkun
 * @date 2019/7/13 0013 13:48
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private SellerService sellerService;
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("经过了UserDetailsServiceImpl...");
        //构建角色列表并返回
        List<GrantedAuthority> grantAuths = new ArrayList<>();
        grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        TbSeller seller = sellerService.findOne(username);
        String trueStatus = "1";
        if (seller!=null){
            return new User(username, seller.getPassword(),trueStatus.equals(seller.getStatus())?true:false,true,true,true,grantAuths);
        }else {
            throw new UsernameNotFoundException(username+"用户未查找到异常");
        }
    }




    /**
     * 获取权限
     * @param roles
     * @return
     */
    public List<SimpleGrantedAuthority> getAuthorities(List<String> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_SELLER")).collect(Collectors.toList());
    }
}
