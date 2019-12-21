package com.guli.ucenter.utils;

import com.guli.ucenter.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author helen
 */
public class JwtUtils {

    public static final String SUBJECT = "guli-user";

    //秘钥
    public static final String APPSECRET = "guli666";

    //过期时间，毫秒，30分钟
    public static final long EXPIRE = 1000 * 60 * 1;

    /**
     * 生成Jwt令牌
     *
     * @param member
     * @return
     */
    public static String generateJWT(Member member) {

        if (member == null
                || StringUtils.isEmpty(member.getId())
                || StringUtils.isEmpty(member.getNickname())
                || StringUtils.isEmpty(member.getAvatar())) {
            return null;
        }

        if (member == null || StringUtils.isEmpty(member.getId())
                || StringUtils.isEmpty(member.getNickname())
                || StringUtils.isEmpty(member.getAvatar())) {
            return null;
        }

        //签名哈希
        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("id", member.getId())
                .claim("nickname", member.getNickname())
                .claim("avatar", member.getAvatar())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();

        return token;
    }


    /**
     * 校验jwt
     *
     * @param jwtToken
     * @return
     */
    public static Claims checkJWT(String jwtToken) {

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();

        return claims;
    }
    //测试token
//    public static void main(String[] args){
//        Member member = new Member();
//
//        member.setId("123");
//        member.setNickname("helen");
//        member.setAvatar("imgpng");
//        String token = generateJWT(member);
//
//        System.err.println(token);
//
//        Claims claims = checkJWT(token);
//        String id = (String) claims.get("id");
//        String name= (String) claims.get("nickname");
//        String avatar= (String) claims.get("avatar");
//        System.err.println("id:"+id);
//        System.err.println("name:"+name);
//        System.err.println("avatar:"+avatar);
//    }
}
