package br.com.user.micro.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenGenerator {
    public final JwtEncoder jwtEncoder;

    @Value("${application.token.lifetime}")
    public Long tokenLifeTime;

    public TokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String tokenConstructor(
            String userId,
            String sessionId,
            String scopeId
    ) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(userId)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(this.tokenLifeTime))
                .claim("scope", scopeId)
                .claim("sessionId", sessionId)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
