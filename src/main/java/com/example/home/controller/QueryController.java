package com.example.home.controller;

import com.example.home.model.QueryResult;
import com.example.home.service.QueryGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private static final Logger log = LoggerFactory.getLogger(QueryController.class);
    
    private final QueryGatewayService gatewayService;

    public QueryController(QueryGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/http/{id}")
    public QueryResult queryViaHttp(@PathVariable("id") String id) {
        log.info("接收到HTTP查询请求，客户ID: {}", id);
        QueryResult result = gatewayService.queryViaHttp(id);
        log.info("HTTP查询完成，返回结果: {}", result);
        return result;
    }

    @GetMapping("/grpc/{id}")
    public QueryResult queryViaGrpc(@PathVariable("id") String id) {
        log.info("接收到gRPC查询请求，客户ID: {}", id);
        QueryResult result = gatewayService.queryViaGrpc(id);
        log.info("gRPC查询完成，返回结果: {}", result);
        return result;
    }
}