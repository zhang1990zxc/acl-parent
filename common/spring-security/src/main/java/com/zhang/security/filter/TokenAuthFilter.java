package com.zhang.security.filter;

import com.zhang.security.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TokenAuthFilter
 * @Description 整条街最靓的仔，写点注释吧
 * @Author 天涯
 * @Date 2021/3/30 20:19
 * @Version 1.0
 **/
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        if (authenticationToken != null) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            String username = tokenManager.getUserinfoFromToken(token);
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            permissionValueList.forEach(permission -> {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
                authorities.add(authority);
            });
            return new UsernamePasswordAuthenticationToken(username, token, authorities);
        }
        return null;
    }
}
