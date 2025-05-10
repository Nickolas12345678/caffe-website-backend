package com.nickolas.caffebackend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtConstant {
    public static final String SECRET_KEY = new String(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    public static final String JWT_HEADER = "Authorization";
}
