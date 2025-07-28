package com.rflwgnr.cabeleleilaleila.services;

import com.rflwgnr.cabeleleilaleila.data.dto.security.AccountCredentialsDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.security.TokenDTO;
import com.rflwgnr.cabeleleilaleila.repository.UserRepository;
import com.rflwgnr.cabeleleilaleila.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!"));

        var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        repository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username " + username + " not found!"));
        TokenDTO token;

        token = tokenProvider.refreshToken(refreshToken);

        return ResponseEntity.ok(token);
    }
}
