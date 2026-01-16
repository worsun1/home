package com.example.home.service;

import com.example.home.grpc.CustomerDto;
import com.example.home.mapper.QueryResultMapper;
import com.example.home.model.QueryResult;
import com.example.home.model.RemoteQueryRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueryGatewayService {

    private final HttpQueryClient httpQueryClient;
    private final GrpcQueryClient grpcQueryClient;
    private final DubboQueryClient dubboQueryClient;
    private final QueryResultMapper queryResultMapper;

    public QueryGatewayService(HttpQueryClient httpQueryClient,
                               GrpcQueryClient grpcQueryClient,
                               DubboQueryClient dubboQueryClient,
                               QueryResultMapper queryResultMapper) {
        this.httpQueryClient = httpQueryClient;
        this.grpcQueryClient = grpcQueryClient;
        this.dubboQueryClient = dubboQueryClient;
        this.queryResultMapper = queryResultMapper;
    }

    public QueryResult queryViaHttp(String id) {
        RemoteQueryRecord record = httpQueryClient.fetchRecord(id);
        if (record == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data returned from HTTP query service");
        }
        return queryResultMapper.mapToResult(record.getId(), record.getName(), record.getEmail());
    }

    public QueryResult queryViaGrpc(String id) {
        CustomerDto customerDto = grpcQueryClient.fetchRecord(id);
        return queryResultMapper.mapToResult(
                String.valueOf(customerDto.getId()),
                customerDto.getName(),
                customerDto.getEmail()
        );
    }

    public QueryResult queryViaDubbo(String id) {
        return dubboQueryClient.fetchRecord(id);
    }
}
