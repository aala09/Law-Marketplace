package tn.esprit.cloud_in_mypocket.service;

import tn.esprit.cloud_in_mypocket.Config.JitsiProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class JitsiService {
  @Autowired 
  private JitsiProperties props;

  // Add getter for properties to use in controller
  public JitsiProperties getProps() {
    return props;
  }

  public String generateJwt(String room, String userId, String displayName) {
    Instant now = Instant.now();
    return Jwts.builder()
      .setAudience("jitsi")
      .setIssuer(props.getAppId())
      .setSubject(userId)
      .claim("context", Map.of(
         "user", Map.of("name", displayName, "id", userId)
      ))
      .setId(UUID.randomUUID().toString())
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plus(1, HOURS)))
      .signWith(Keys.hmacShaKeyFor(props.getAppSecret().getBytes()), SignatureAlgorithm.HS256)
      .compact();
  }
}
