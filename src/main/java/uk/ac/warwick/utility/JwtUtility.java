package uk.ac.warwick.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import uk.ac.warwick.domain.AuthenticationResponse;
import uk.ac.warwick.model.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JwtUtility {

    private static final String JWT_SYMMETRIC_KEY = "secret";
    private static final long TOKEN_EXPIRATION_DELTA = 1000 * 60 * 30;

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        if (isNull(claims)) {
            return null;
        }
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(JWT_SYMMETRIC_KEY).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            return null;
        }
        return claims;
    }

    public static boolean validateToken(String token, Account account) {
        return nonNull(account) && nonNull(extractUsername(token)) && !isTokenExpired(token);
    }

    public static Boolean isTokenExpired(String token) {
        if (isNull(token)) {
            return true;
        }
        Date expiration = extractExpiration(token);
        if (isNull(expiration)) {
            return null;
        }
        return expiration.before(new Date());
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_DELTA))
                .signWith(SignatureAlgorithm.HS256, JWT_SYMMETRIC_KEY).compact();
    }

    public static AuthenticationResponse generateToken(Account details) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, details.getUsername());
        return new AuthenticationResponse(token);
    }

}
