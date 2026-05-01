package br.com.user.micro.util;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenCredentialsExtractor {
    private final JwtDecoder jwtDecoder;

    public TokenCredentialsExtractor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public HashMap<String, String> extractor(String authorization) {
        String token = authorization.replace("Bearer", "").trim();
        Jwt tokenInfo = jwtDecoder.decode(token);
        String role = tokenInfo.getClaim("role");
        String userId = tokenInfo.getSubject();
        String sessionId = tokenInfo.getClaim("sessionId");

        return new HashMap<>(Map.of(
                "id", userId,
                "role", role,
                "sessionId", sessionId
        ));
    }

    public Boolean userValidator(String userId, String authorization) {
        String token = authorization.replace("Bearer", "").trim();
        Jwt tokenInfo = jwtDecoder.decode(token);
        String id = tokenInfo.getSubject();

        return id.equals(userId);
    }
}
