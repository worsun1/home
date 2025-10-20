package com.example.home.service;

import com.example.home.proto.GetCustomerRequest;
import com.example.home.proto.GetCustomerResponse;
import com.example.home.proto.CustomerServiceGrpc;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
public class GrpcQueryClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcQueryClient.class);

    private final CustomerServiceGrpc.CustomerServiceBlockingStub blockingStub;

    public GrpcQueryClient(CustomerServiceGrpc.CustomerServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public GetCustomerResponse fetchRecord(String id) {
        log.info("构建gRPC请求，客户ID: {}", id);
        GetCustomerRequest request = GetCustomerRequest.newBuilder().setId(Long.parseLong(id)).build();
        log.info("发送gRPC请求: {}", request);
        try {
            GetCustomerResponse response = blockingStub.getCustomer(request);
            log.info("gRPC请求成功，返回响应: {}", response);
            return response;
        } catch (StatusRuntimeException ex) {
            log.error("gRPC查询请求失败，客户ID: {}", id, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    String.format("gRPC query service returned status %s", ex.getStatus()),
                    ex);
        }
    }
}