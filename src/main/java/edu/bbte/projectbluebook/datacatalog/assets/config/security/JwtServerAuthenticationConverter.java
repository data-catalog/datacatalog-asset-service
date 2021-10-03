package edu.bbte.projectbluebook.datacatalog.assets.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(ex -> Mono.justOrEmpty(ex.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)))
                .filter(headerValue -> headerValue.startsWith(TOKEN_PREFIX))
                .map(headerValue -> headerValue.replace(TOKEN_PREFIX, ""))
                .map(token -> new UsernamePasswordAuthenticationToken(token, token));
    }
}
