package com.mja123.security.domain.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mja123.security.domain.dto.LoginResponseDTO;
import com.mja123.security.domain.dto.SignUpRequestDTO;
import com.mja123.security.domain.dto.SignUpResponseDTO;
import com.mja123.security.exceptions.InvalidCredentialsException;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import com.mja123.security.exceptions.SignUpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String clientId;
    private final String clientSecret;
    private final String audience;
    private final String connection;

    public AuthService(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            @Value("${auth0.client-id}") String clientId,
            @Value("${auth0.client-secret}") String clientSecret,
            @Value("${spring.security.oauth2.resourceserver.jwt.audience}") String audience,
            @Value("${auth0.connection:Username-Password-Authentication}") String connection
    ) {
        this.objectMapper = objectMapper;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.audience = audience;
        this.connection = connection;
        this.restClient = restClientBuilder.baseUrl(issuerUri).build();
    }

    public LoginResponseDTO login(String email, String password) {
        Map<String, String> body = Map.of(
                "grant_type", "http://auth0.com/oauth/grant-type/password-realm",
                "username", email,
                "password", password,
                "audience", audience,
                "scope", "openid offline_access",
                "client_id", clientId,
                "client_secret", clientSecret,
                "realm", connection
        );

        Auth0TokenResponse response = restClient.post()
                .uri("oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    String raw = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.warn("Auth0 login error: {}", raw);
                    throw new InvalidCredentialsException("Invalid email or password.");
                })
                .body(Auth0TokenResponse.class);

        return toLoginResponse(response);
    }

    public LoginResponseDTO refresh(String refreshToken) {
        Map<String, String> body = Map.of(
                "grant_type", "refresh_token",
                "client_id", clientId,
                "client_secret", clientSecret,
                "refresh_token", refreshToken
        );

        Auth0TokenResponse response = restClient.post()
                .uri("oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new InvalidCredentialsException("Invalid or expired refresh token.");
                })
                .body(Auth0TokenResponse.class);

        return toLoginResponse(response);
    }

    public SignUpResponseDTO signUp(SignUpRequestDTO request) {
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("email", request.email());
        body.put("password", request.password());
        body.put("connection", connection);
        if (request.firstName() != null) body.put("given_name", request.firstName());
        if (request.lastName() != null) body.put("family_name", request.lastName());

        Auth0SignUpResponse response = restClient.post()
                .uri("dbconnections/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    String raw = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.warn("Auth0 signup error: {}", raw);

                    JsonNode error = objectMapper.readTree(raw);

                    String code = error.path("code").asText(null);
                    if ("user_exists".equals(code)) {
                        throw new NotUniqueAttributeException("Email is already registered.");
                    }
                    if ("invalid_password".equals(code)) {
                        String policy = error.path("policy").asText(null);
                        throw new SignUpException(policy != null
                                ? "Password does not meet requirements: " + policy
                                : "Password is too weak. It must be at least 8 characters and include uppercase, lowercase, numbers, and special characters.");
                    }

                    String message = error.path("message").asText(null);
                    if (message == null) {
                        JsonNode desc = error.path("description");
                        if (desc.isTextual()) message = desc.asText();
                    }
                    throw new SignUpException(message != null ? message : raw);
                })
                .body(Auth0SignUpResponse.class);

        return new SignUpResponseDTO(
                response.email(),
                Boolean.TRUE.equals(response.emailVerified())
        );
    }

    private LoginResponseDTO toLoginResponse(Auth0TokenResponse response) {
        return new LoginResponseDTO(
                response.accessToken(),
                response.refreshToken(),
                response.tokenType(),
                response.expiresIn()
        );
    }

    private record Auth0TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") Integer expiresIn
    ) {}

    private record Auth0SignUpResponse(
            @JsonProperty("_id") String id,
            @JsonProperty("email") String email,
            @JsonProperty("email_verified") Boolean emailVerified
    ) {}
}
