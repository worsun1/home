package com.example.home.service;

import com.example.home.dubbo.CustomerDubboService;
import com.example.home.dubbo.dto.CustomerDTO;
import com.example.home.model.QueryResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Component
public class DubboQueryClient {

  @DubboReference
  private CustomerDubboService customerDubboService;

  public QueryResult fetchRecord(String id) {
    try {
      CustomerDTO customer = customerDubboService.getCustomer(Long.valueOf(id));
      return new QueryResult(customer.getId(), customer.getName(), customer.getEmail());
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
          "Dubbo query service error: " + ex.getMessage(), ex);
    }
  }

  public List<QueryResult> searchCustomers(String name, String email) {
    try {
      List<CustomerDTO> customers = customerDubboService.searchCustomers(name, email);
      return customers.stream()
          .map(customer -> new QueryResult(customer.getId(), customer.getName(), customer.getEmail()))
          .toList();
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
          "Dubbo query service error: " + ex.getMessage(), ex);
    }
  }
}