package com.zhang.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName TokenManager
 * @Description 整条街最靓的仔，写点注释吧
 * @Author 天涯
 * @Date 2021/3/30 19:11
 * @Version 1.0
 **/
@Component
public class TokenManager {

    private final long tokenExpiration = 24 * 60 * 60 * 1000;

    private final String tokenSignKey = "123456";

    // 1 根据用户名生成token
    public String createToken(String username) {
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.ES512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    // 2 根据token字符串得到用户信息
    public String getUserinfoFromToken(String token) {
        String userinfo = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userinfo;
    }

    // 删除token
    public void removeToken(String token) {}

}
