package com.example.home.config;

import com.example.home.proto.QueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(QueryClientProperties.class)
public class ClientConfiguration {

    private final QueryClientProperties properties;

    public ClientConfiguration(QueryClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel queryManagedChannel() {
        QueryClientProperties.Grpc grpc = properties.getGrpc();
        return ManagedChannelBuilder.forAddress(grpc.getHost(), grpc.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public QueryServiceGrpc.QueryServiceBlockingStub queryServiceBlockingStub(ManagedChannel queryManagedChannel) {
        return QueryServiceGrpc.newBlockingStub(queryManagedChannel);
    }
}
