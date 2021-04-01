package com.zhang.aclservice.service.impl;

import com.zhang.aclservice.entity.User;
import com.zhang.aclservice.service.PermissionService;
import com.zhang.aclservice.service.UserService;
import com.zhang.security.entity.SecurityUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserDetailServiceImpl
 * @Description 整条街最靓的仔，写点注释吧
 * @Author 天涯
 * @Date 2021/3/30 21:08
 * @Version 1.0
 **/
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        com.zhang.security.entity.User curUser = new com.zhang.security.entity.User();
        BeanUtils.copyProperties(user, curUser);
        List<String> list = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(list);
        return securityUser;
    }
}
