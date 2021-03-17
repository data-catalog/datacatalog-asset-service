package edu.bbte.projectbluebook.datacatalog.assets.client;

import edu.bbte.projectbluebook.datacatalog.assets.config.ClientProperties;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.TokenInfoRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.TokenInfoResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(ClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        final String uri = clientProperties.getUserServiceUri();

        this.webClient = webClientBuilder.baseUrl(uri).build();
    }

    public Mono<TokenInfoResponse> getTokenInfo(String token) {
        TokenInfoRequest request = new TokenInfoRequest().token(token);

        return webClient
                .post().uri("/token_info")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TokenInfoResponse.class);
    }
}
