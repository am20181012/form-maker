package com.app.security2023.auth.config;

import com.app.security2023.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * kada primimo neki HTTP zahtev, prva stvar koja se primenjuje jeste filter, odnosno JwtAuthFilter
 *
 * kako zelimo da svaki put kada stigne neki HTTP zahtev aktiviramo i filter da se izvrsi
 * koristimo klasu OncePerRequestFilter (filter po svakom zahtevu)
 *
 * da bi Spring znao da ovaj filter postoji moramo ga oznaciti kao managed bean - odnosno dodati anotaciju @Component
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Klasa koja obradjuje JWT token
     */
    private final JwtService jwtService;
    /**
     *
     */
    private final UserDetailsService userDetailsService;

    /**
     *
     * @param request
     * @param response
     * @param filterChain - Chain of Responsibility Pattern - predstavlja listu ostalih filtera koje treba izvrsiti
     * @throws ServletException
     * @throws IOException
     *
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        /**
         * authHeader - kada saljemo zahtev moramo da u header-u tog zahteva prosledimo token za autentifikaciju (jwt token)
         *              pa pokusavamo da otpakujemo taj header koji se zove "Authorization"
         */
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        /**
         * prvu stvar koju radimo u ovom filteru proveravamo da li imamo JWT token
         *
         * ukoliko nemamo header ili nas token ne pocinje sa "Bearer " onda samo pozivamo sledeci filter u nizu da se izvrsi,
         * a za ovaj filter ovde se izvrsavanje zavrsava
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * sada uzimamo JWT token iz header-a
         */
        jwtToken = authHeader.substring(7);

        // extract userEmail from JWT token
        userEmail = jwtService.extractUsername(jwtToken);

        /**
         * proveravamo da li postoji username u nasem tokenu i da li je User vec autentifikovan
         * jer ako jeste ne moramo da sve radimo...
         *
         * ukoliko User nije autentifikovan onda moramo da proverimo User-a iz DB-a
         * ukoliko pokupimo User-a iz baze, onda moramo da vidimo da li je token validan
         * ukoliko jeste kreiramo objekat tipa UsernamePasswordAuthenticationToken i ovaj objekat nam
         * je potreban kako bismo update-ovali nas SecurityHolderContext
         *
         */
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // ?????
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            filterChain.doFilter(request, response);
        }
    }
}
