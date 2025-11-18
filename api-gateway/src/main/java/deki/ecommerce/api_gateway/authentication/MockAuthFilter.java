package deki.ecommerce.api_gateway.authentication;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MockAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // mock user-id
        exchange.getRequest()
                .mutate()
                .header("X-User-Id", "demo-user")
                .build();

        return chain.filter(exchange);
    }
}