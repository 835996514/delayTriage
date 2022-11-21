package com.rc.temp.util;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    //有效期
    public static final Long JWT_TTL = 60*60*24*3*1000L;

    //设置密钥明文
    public static final String JWT_KEY = "liaohj";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    public static String createJWT(String subject){
        JwtBuilder builder = getJwtBuilder(subject,null,getUUID());
        return builder.compact();
    }

    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }
    /**
     * @param subject token中要存放的数据(json格式)
     * @param ttlMillis token超时时间
     * @param uuid
     * @return 生成jwt
     */
    private static JwtBuilder getJwtBuilder(String subject,Long ttlMillis,String uuid){
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        if(ttlMillis == null){
            ttlMillis= JwtUtil.JWT_TTL;
        }
        long expMillis = timeMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)            //唯一ID
                .setSubject(subject)    //主题，可以是JSON数据
                .setIssuer("lhj")       //签发者
                .setIssuedAt(date)      //签发时间
                .signWith(algorithm,secretKey)  //使用HS256对称加密算法签名，secretKey为密钥
                .setExpiration(expDate);

    }

    //生成加密后的密钥
    public static SecretKey generalKey(){
        byte[] encodedKey= Base64.getDecoder().decode(JWT_KEY);
        SecretKeySpec keySpec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return keySpec;
    }

    public static Claims parseJWT(String jwt){
        SecretKey secretKey = generalKey();
        Claims claims;
        try {
            claims = Jwts.parser()                //获取jwt解析器
                    .setSigningKey(secretKey)   //给key设置密钥
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (ExpiredJwtException e){
            //token过期之后会直接抛出异常不返回claims对象
            claims = e.getClaims();
        }
        return claims;
    }
}
