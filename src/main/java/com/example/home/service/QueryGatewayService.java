package com.example.home.service;

import com.example.home.mapper.QueryResultMapper;
import com.example.home.model.QueryResult;
import com.example.home.model.RemoteQueryRecord;
import com.example.home.proto.GetCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueryGatewayService {

    private static final Logger log = LoggerFactory.getLogger(QueryGatewayService.class);
    
    private final HttpQueryClient httpQueryClient;
    private final GrpcQueryClient grpcQueryClient;
    private final QueryResultMapper queryResultMapper;

    public QueryGatewayService(HttpQueryClient httpQueryClient,
                               GrpcQueryClient grpcQueryClient,
                               QueryResultMapper queryResultMapper) {
        this.httpQueryClient = httpQueryClient;
        this.grpcQueryClient = grpcQueryClient;
        this.queryResultMapper = queryResultMapper;
    }

    public QueryResult queryViaHttp(String id) {
        log.info("通过HTTP查询客户信息，客户ID: {}", id);
        RemoteQueryRecord record = httpQueryClient.fetchRecord(id);
        if (record == null) {
            log.error("HTTP查询未返回数据，客户ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data returned from HTTP query service");
        }
        log.info("HTTP查询成功获取客户信息: {}", record);
        QueryResult result = queryResultMapper.mapToResult(record.getId(), record.getName(), record.getEmail());
        log.info("HTTP查询结果映射完成: {}", result);
        return result;
    }

    public QueryResult queryViaGrpc(String id) {
        log.info("通过gRPC查询客户信息，客户ID: {}", id);
        GetCustomerResponse response = grpcQueryClient.fetchRecord(id);
        log.info("gRPC查询成功获取客户信息: {}", response.getCustomer());
        QueryResult result = queryResultMapper.mapToResult(
            String.valueOf(response.getCustomer().getId()), 
            response.getCustomer().getName(), 
            response.getCustomer().getEmail()
        );
        log.info("gRPC查询结果映射完成: {}", result);
        return result;
    }
}