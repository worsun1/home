package com.example.home.service;

import com.example.home.grpc.CustomerDto;
import com.example.home.grpc.CustomerServiceGrpc;
import com.example.home.grpc.GetCustomerRequest;
import com.example.home.grpc.GetCustomerResponse;
import com.example.home.grpc.SearchCustomersRequest;
import com.example.home.grpc.SearchCustomersResponse;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Component
public class GrpcQueryClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcQueryClient.class);

    private final CustomerServiceGrpc.CustomerServiceBlockingStub blockingStub;

    public GrpcQueryClient(CustomerServiceGrpc.CustomerServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public CustomerDto fetchRecord(String id) {
        GetCustomerRequest request = GetCustomerRequest.newBuilder().setId(Long.parseLong(id)).build();
        try {
            GetCustomerResponse response = blockingStub.getCustomer(request);
            return response.getCustomer();
        } catch (StatusRuntimeException ex) {
            log.error("gRPC query request failed for id {}", id, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    String.format("gRPC query service returned status %s", ex.getStatus()),
                    ex);
        }
    }

    public List<CustomerDto> searchCustomers(String name, String email) {
        SearchCustomersRequest request = SearchCustomersRequest.newBuilder()
                .setName(name != null ? name : "")
                .setEmail(email != null ? email : "")
                .build();
        try {
            SearchCustomersResponse response = blockingStub.searchCustomers(request);
            return new ArrayList<>(response.getCustomersList());
        } catch (StatusRuntimeException ex) {
            log.error("gRPC search request failed for name: {}, email: {}", name, email, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    String.format("gRPC query service returned status %s", ex.getStatus()),
                    ex);
        }
    }
}
