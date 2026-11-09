package microservice.Order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Common place for ProductServiceClientConfig and userRestClientInterface beans

    @Bean
    @Primary // 🔄 Eureka and everything else gets the plain builder
    public RestClient.Builder plainRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    @LoadBalanced // ✅ only used where you explicitly ask for it
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}
