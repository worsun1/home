package com.example.home.controller;

import com.example.home.model.QueryResult;
import com.example.home.service.QueryGatewayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryGatewayService gatewayService;

    public QueryController(QueryGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/http/{id}")
    public QueryResult queryViaHttp(@PathVariable("id") String id) {
        return gatewayService.queryViaHttp(id);
    }

    @GetMapping("/grpc/{id}")
    public QueryResult queryViaGrpc(@PathVariable("id") String id) {
        return gatewayService.queryViaGrpc(id);
    }
}
