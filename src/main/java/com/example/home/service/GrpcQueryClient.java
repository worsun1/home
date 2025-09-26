package com.example.home.service;

import com.example.home.proto.QueryRequest;
import com.example.home.proto.QueryResponse;
import com.example.home.proto.QueryServiceGrpc;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
public class GrpcQueryClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcQueryClient.class);

    private final QueryServiceGrpc.QueryServiceBlockingStub blockingStub;

    public GrpcQueryClient(QueryServiceGrpc.QueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public QueryResponse fetchRecord(String id) {
        QueryRequest request = QueryRequest.newBuilder().setId(id).build();
        try {
            return blockingStub.getById(request);
        } catch (StatusRuntimeException ex) {
            log.error("gRPC query request failed for id {}", id, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    String.format("gRPC query service returned status %s", ex.getStatus()),
                    ex);
        }
    }
}
