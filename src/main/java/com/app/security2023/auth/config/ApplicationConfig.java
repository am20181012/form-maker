package com.app.security2023.auth.config;

import com.app.security2023.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /**
     *
     * @return
     * 1. potreban u klasi JwtAuthFilter
     *
     *
     * implementacija UserDetailsService-a, odnosno obezbedjivanje Bean-a tipa UserDetailsService koji nam je potreban
     * u klasi JwtAuthFilter kako bismo implementirali metodu loadUserByUsername
     *
     * posto UserDetailsService je funkcionalni interfejs (interfejs koji sadrzi samo jednu apstraktnu metodu), onda
     * implemetaciju te metode mozemo dati preko lambda izraza
     */
    @Bean
    public UserDetailsService userDetailsService(){
        // implementacija metode loadUserByUsername
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     *
     * @return
     * 2. potreban u klasi SecurityConfiguration
     *
     * provider je ustvari Data Access Object ili DAO koji je odgovoran za fecovanje podataka userDetails objekta, enkodovanje lozinke...
     *
     * mi ovde ukazujemo da zelimo da koristimo implementaciju DaoAuthenticationProvider klase
     * potrebno je da mu podesimo service koji ce koristiti prilikom fecovanja User-a
     * potrebno je takodje da podesimo i passwordEncoder koji zelimo da koristimo kako bismo citali i upisivali lozinku
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     *
     * @param config
     * @return
     * @throws Exception
     *
     * 4. odgovoran za upravljanje autentikacijom, u ovaj bean indzektujemo obj tipa AuthenticationConfiguration
     * AuthenticationConfiguration vec sadrzi info o AuthenticationManager-u ... ?????
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     *
     * @return
     * 3. kako bismo indzektovali u authenticationProvider
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
