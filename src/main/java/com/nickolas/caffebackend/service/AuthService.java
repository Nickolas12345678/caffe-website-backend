package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.config.JwtProvider;
import com.nickolas.caffebackend.exception.UserException;
import com.nickolas.caffebackend.model.User;
import com.nickolas.caffebackend.domain.Role;
import com.nickolas.caffebackend.repository.UserRepository;
import com.nickolas.caffebackend.request.SigninRequest;
import com.nickolas.caffebackend.request.SignupRequest;
import com.nickolas.caffebackend.response.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider,  AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }


    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserException("Користувач з таким email вже існує!");
        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }


    public String generateToken(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, List.of(new SimpleGrantedAuthority( user.getRole().name()))
        );

        return jwtProvider.generateToken(authentication);
    }

    public AuthResponse authenticateUser(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Користувача з таким email не існує"));
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new UserException("Невірний пароль");
        }
        String token = generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setRole(user.getRole().name());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.getEmail(),
                signinRequest.getPassword()
        ));

        return authResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserException("Користувача з таким email не існує"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
