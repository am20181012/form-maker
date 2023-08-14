package com.app.security2023.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    /**
     * da bismo proverili da li je posiljalac onaj koji tvrdi da jeste
     */
//     @Value("${secret.key}")
//     private static String SECRET_KEY;

    private static final String SECRET_KEY = "472D4B614E645267556B58703273357638792F423F4528482B4D625165536856";

    /**
     *
     * @param token
     * @return
     *
     *     4. ovde samo uzimamo konkretni claim Subject (username naseg User-a) preko genericke metode extractClaim
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     *
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     *
     *     3. metoda koja uzima konkretni Claim, tako sto iz tokena pokupi claim-ove i primeni prosledjenu funkciju na claim-ove
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     *
     * @param userDetails
     * @return
     *
     *  kada zelimo da generisemo token bez dodatnih claim-ova (extraClaims)
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     *
     * @param extraClaims - neki dodatni claim-ovi koje zelimo da dodamo
     * @param userDetails
     * @return
     *
     *  generisemo token tako sto samo setujemo dodatne claim-ove, Subject, kada je token kreiran, kada token istice,
     *  koji kljuc zelimo da koristimo kao i SignatureAlgoritam
     *
     *  compact() - generise i vraca token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 24)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * @param token
     * @param userDetails - potrebno nam je da proverimo da li se user-i poklapaju
     * @return
     *
     *  metoda koja proverava (validira) token - da li se user-i poklapaju i da li je token istekao
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     *
     * @param token
     * @return
     *
     *  1. metoda koja uzima sve Claim-ove iz prosledjenog tokena
     */
    private Claims extractAllClaims(String token) {
        // sth like this Jwts jwts = new JwtsImpl(signingKey);
        //               return jwts.parseClaimsJws(token).getBody();
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey()) // kada generisemo token potreban nam je signing key, pa da bismo dobili claim-ove moramo da proverimo signing key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     *
     * @return
     * 2.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     *
     * @param token
     * @return
     *
     *  samo proveravamo da li je datum isteka se desio pre sadasnjeg trenutka
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     *
     * @param token
     * @return
     *  vraca nam datum isteka tokena tako sto samo pozovemo genericku metodu extractClaim
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
