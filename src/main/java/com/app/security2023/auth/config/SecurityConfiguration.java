package com.app.security2023.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 *   moramo da kazemo Spring-u koju konfiguraciju da koristimo kako bi filter bio primenjen uopste
 *   mi imamo filter ali se jos uvek ne koristi
 *
 *   kada se aplikacija pokrene Spring Security ce prvo potraziti Bean tipa SecurityFilterChain a ovaj Bean
 *   je odgovoran za konfiguraciju svih stvari koje se ticu zastite HTTP zahteva ili tako nesto ?????
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    /**
     *  ovog providera moramo da impelementiramo negde, tj da ga definisemo kao Bean
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     *
     * @param http
     * @return
     * @throws Exception
     *
     *  http
     *                 .csrf()
     *                 .disable()
     *                 .authorizeHttpRequests()
     *                 .requestMatchers("/api/v1/auth/**") - white list (URL-ovi kojima nije potrebna autentifikacija)
     *                 .permitAll() - ovime dozvoljavamo da svi zahtevi sa liste mogu da prodju bez autentifikacije
     *                 .anyRequest()
     *                 .authenticated() - ali ovime kazemo da bilo koji drugi zahtev koji nije na white list mora da bude autentifikovan
     *                 .and()
     *                 .sessionManagement()
     *                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS) - kako primenjujemo OncePerRequestFilter znaci da ne moramo
     *                                                                           da cuvamo stanje sesije i to nam pomaze da osiguramo da
     *                                                                           ce svaki od ostalih zahteva zahtevati autentifikaciju
     *                                                                           (Spring kreira novu sesiju za svaki zahtev)
     *                 .and()
     *                 .authenticationProvider(authenticationProvider) - kojeg AuthenticationProvider-a zelimo da koristimo
     *                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); - sada konacno primenjujemo JwtAuthFilter
     *                                                                                              dodajemo ovaj filter pre jer zelimo da ga
     *                                                                                              izvrsimo pre UsernamePasswordAuthenticationFilter-a
     *                                                                                              jer prvo isproveravamo sve vezano za jwt, a
     *                                                                                              onda pozivamo ovaj drugi filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
               // .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/images/**", "/api/v1/auth/**", "/api/v1/forms/form-by-slug/*", "/api/v1/forms/answer/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
//                "Authorization", "X-Requested-With", "requestId", "Correlation-Id"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
