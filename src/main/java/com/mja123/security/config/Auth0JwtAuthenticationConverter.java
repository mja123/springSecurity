package com.mja123.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom JWT authentication converter for Auth0.
 * Extracts authorities from both 'permissions' claim and roles from namespace claim.
 * 
 * Auth0 typically provides:
 * - 'permissions' array: Custom permissions (e.g., "read:users", "write:users")
 * - Namespace claim (e.g., 'https://your-domain.auth0.com/roles'): Array of roles
 */
@Component
public class Auth0JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultConverter;
    
    @Value("${auth0.roles-claim-namespace:}")
    private String rolesClaimNamespace;

    public Auth0JwtAuthenticationConverter() {
        this.defaultConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultConverter.convert(jwt).stream(),
                extractAuthorities(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extracts authorities from JWT claims.
     * Handles both permissions and roles.
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new java.util.ArrayList<>();

        // Extract permissions from 'permissions' claim
        List<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions != null) {
            authorities.addAll(permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority("SCOPE_" + permission))
                    .toList());
        }

        // Extract roles from namespace claim (e.g., 'https://your-domain.auth0.com/roles')
        if (rolesClaimNamespace != null && !rolesClaimNamespace.isEmpty()) {
            Object rolesClaim = jwt.getClaim(rolesClaimNamespace);
            if (rolesClaim instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) rolesClaim;
                authorities.addAll(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .toList());
            } else if (rolesClaim instanceof String) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + rolesClaim.toString().toUpperCase()));
            }
        }

        // Fallback: try to extract roles from 'roles' claim if namespace not provided
        if (rolesClaimNamespace == null || rolesClaimNamespace.isEmpty()) {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                authorities.addAll(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .toList());
            }
        }

        return authorities;
    }
}

