package com.nickolas.caffebackend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Клас з константами для налаштування JWT.
 *
 * <p>Містить секретний ключ для підпису JWT та назву заголовка, з якого зчитується токен.</p>
 */
public class JwtConstant {
    public static final String SECRET_KEY = new String(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    public static final String JWT_HEADER = "Authorization";
}
