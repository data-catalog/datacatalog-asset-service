package edu.bbte.projectbluebook.datacatalog.assets.config.security;

import edu.bbte.projectbluebook.datacatalog.assets.exception.NotFoundException;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Autowired
    JwtAuthenticationManager authenticationManager;

    @Autowired
    JwtServerAuthenticationConverter authenticationConverter;

    @Autowired
    AssetService assetService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        return http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/assets/search/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/assets").permitAll()
                .pathMatchers(HttpMethod.POST, "/assets").authenticated()
                .pathMatchers(HttpMethod.GET, "/assets/{assetId}").access(this::isMemberOrPublic)
                .pathMatchers(HttpMethod.PATCH, "/assets/{assetId}").access(this::isMemberOrAdmin)
                .pathMatchers(HttpMethod.DELETE, "/assets/{assetId}").access(this::isMemberOrAdmin)
                .pathMatchers(HttpMethod.POST, "/assets/{assetId}/tags/{tag}").access(this::isMemberOrAdmin)
                .pathMatchers(HttpMethod.DELETE, "/assets/{assetId}/tags/{tag}").access(this::isMemberOrAdmin)
                .pathMatchers("/assets/favorites/**").authenticated()
                .anyExchange().permitAll()
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private Mono<AuthorizationDecision> isMember(Mono<Authentication> authentication, AuthorizationContext context) {
        Mono<List<String>> assetMembers = assetService
                .getAsset(context.getVariables().get("assetId").toString())
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .map(assetResponse -> {
                    List<String> members = assetResponse.getMembers();
                    members.add(assetResponse.getOwnerId());
                    return members;
                });

        Mono<String> principal = authentication.map(Authentication::getPrincipal).cast(String.class);

        return assetMembers.zipWith(principal)
                .map(tuple -> tuple.getT1().contains(tuple.getT2()))
                .defaultIfEmpty(false)
                .map(AuthorizationDecision::new);
    }

    private Mono<AuthorizationDecision> isMemberOrPublic(Mono<Authentication> authentication, AuthorizationContext context) {
        Mono<AssetResponse> assetResponse = assetService
                .getAsset(context.getVariables().get("assetId").toString())
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")));

        Mono<String> principal = authentication.map(Authentication::getPrincipal).cast(String.class).defaultIfEmpty("");

        return assetResponse.zipWith(principal)
                .map(tuple -> tuple.getT1().getPublic()
                        || tuple.getT1().getMembers().contains(tuple.getT2())
                        || tuple.getT1().getOwnerId().equals(tuple.getT2()))
                .defaultIfEmpty(false)
                .map(AuthorizationDecision::new);
    }

    private Mono<AuthorizationDecision> isAdmin(Mono<Authentication> authentication) {
        return authentication
                .map(Authentication::getAuthorities)
                .map(authorities -> authorities.stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")))
                .defaultIfEmpty(false)
                .map(AuthorizationDecision::new);
    }

    private Mono<AuthorizationDecision> isMemberOrAdmin(Mono<Authentication> authentication,
                                                       AuthorizationContext context) {
        return isAdmin(authentication)
                .zipWith(isMember(authentication, context))
                .map(tuple -> tuple.getT1().isGranted() || tuple.getT2().isGranted())
                .map(AuthorizationDecision::new);
    }
}
