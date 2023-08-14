package com.app.security2023.auth.service;

import com.app.security2023.auth.dto.AuthRequest;
import com.app.security2023.auth.dto.AuthResponse;
import com.app.security2023.auth.dto.RegisterRequest;
import com.app.security2023.auth.model.*;
import com.app.security2023.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    /**
     *
     * @param req
     * @return
     *
     * ova metoda cuva User-a u bazi i vraca generisani token
     */
    public AuthResponse register(RegisterRequest req) {

        if(!Objects.equals(req.password(), req.passwordConfirmation())){
            throw new RuntimeException("Password doesn't match. Try again!");
        }

        if(repository.existsByEmail(req.email())) {
            throw new RuntimeException("Invalid email address!");
        }

        // kreiramo User-a na osnovu zahteva
        var user = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .build();

        // cuvamo User-a u db
        User repoUser = repository.save(user);

        // ukoliko uspemo da sacuvamo User-a - generisanje tokena
        var jwt = jwtService.generateToken(user);

        // vracamo token
        return AuthResponse.builder()
                .id(repoUser.getId())
                .firstName(repoUser.getFirstName())
                .lastName(repoUser.getLastName())
                .email(repoUser.getEmail())
                .token(jwt)
                .build();
    }

    public AuthResponse authenticate(AuthRequest req) {

        // AuthenticationManager Bean ima metodu koja nam omogucava da autentifikujemo User-a na osnovu username-a i password-a
        // ova metoda radi ceo posao za nas
        // u slucaju da username ili password nisu tacni bacice exception
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.email(),
                            req.password()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Incorrect username or password");
        }

        // ako dodjemo do ovog dela znaci da je User autentifikovan, odnosno da su username i password tacni

        // prvo pokusavamo da dodjemo do User-a
        var user = repository.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("Incorrect username or password"));

        var jwt = jwtService.generateToken(user);

        return AuthResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .token(jwt)
                .build();
    }
}
