package microservice.Order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {

//    @Bean
//    @Primary // 🔄 Eureka and everything else gets the plain builder
//    public RestClient.Builder plainRestClientBuilder() {
//        return RestClient.builder();
//    }
//
//    @Bean
//    @LoadBalanced // ✅ only used where you explicitly ask for it
//    public RestClient.Builder loadBalancedRestClientBuilder() {
//        return RestClient.builder();
//    }

    @Bean
    public ProductServiceClient productRestClientInterface(@LoadBalanced RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder
                .baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        ((request, response) -> Optional.empty()))
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        ProductServiceClient productServiceClient = factory.createClient(ProductServiceClient.class);
        return productServiceClient;
    }
}
