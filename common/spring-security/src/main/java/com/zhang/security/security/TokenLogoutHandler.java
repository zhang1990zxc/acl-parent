package com.zhang.security.security;

import com.zhang.utils.utils.R;
import com.zhang.utils.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName TokenLogoutHandler
 * @Description 整条街最靓的仔，写点注释吧
 * @Author 天涯
 * @Date 2021/3/30 19:24
 * @Version 1.0
 **/
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (token != null) {
            tokenManager.removeToken(token);
            String username = tokenManager.getUserinfoFromToken(token);
            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, R.ok());
    }
}
