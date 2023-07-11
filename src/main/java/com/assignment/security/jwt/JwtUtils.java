//package com.assignment.security.jwt;
//
//import com.assignment.security.service.UserDetail;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseCookie;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.WebUtils;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtils {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
//
//    @Value("${web.shop.jwtCookieName}")
//    private String jwtCookie;
//
//    @Value("${web.shop.jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${web.shop.jwtExpirationMs}")
//    private int jwtExpirationMs;
//
//    public String getJwtFromCookies(HttpServletRequest request) {
//        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
//        if(cookie != null) {
//            return cookie.getValue();
//        } else {
//            return null;
//        }
//    }
//
//    private Key key() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//    }
//
//    public String generateTokenFromUserName(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//
//
//    public ResponseCookie generateJwtCookies(UserDetail userDetail) {
//
//        String jwt = generateTokenFromUserName(userDetail.getUsername());
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/login")
//                .maxAge(24 * 60 * 60).httpOnly(true).build();
//        return cookie;
//    }
//
//    public ResponseCookie getCleanJwtCookie() {
//
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/login").build();
//        return cookie;
//    }
//
//    public String getUserNameFromJwtToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(key()).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public boolean validateJwtToken(String authToken) {
//
//        try{
//            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
//        } catch (MalformedJwtException e) {
//            logger.error("Invalid JWT token: {}", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            logger.error("JWT token is expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            logger.error("JWT token is unsupported: {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            logger.error("JWT claims string is empty: {}", e.getMessage());
//        }
//        return false;
//    }
//
//}
