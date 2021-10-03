package edu.bbte.projectbluebook.datacatalog.assets.config.security;

import edu.bbte.projectbluebook.datacatalog.assets.client.UserServiceClient;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.TokenInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return userServiceClient.getTokenInfo(authentication.getCredentials().toString())
                .filter(TokenInfoResponse::getActive)
                .switchIfEmpty(Mono.error(new BadCredentialsException("The JWT token is invalid or expired.")))
                .map(response -> new UsernamePasswordAuthenticationToken(
                        response.getUserId(),
                        authentication.getCredentials(),
                        createAuthorities(response.getRole())))
                .cast(Authentication.class);
    }

    private Collection<GrantedAuthority> createAuthorities(TokenInfoResponse.RoleEnum role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getValue().toUpperCase(Locale.ENGLISH)));
    }
}
