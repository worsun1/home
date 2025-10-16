package com.example.home.dubbo;

import com.example.home.dubbo.dto.CustomerDTO;

import java.util.List;

public interface CustomerDubboService {

  /**
   * Get customer by ID
   * 
   * @param id customer ID
   * @return CustomerDTO
   */
  CustomerDTO getCustomer(Long id);

  /**
   * Search customers by name and email
   * 
   * @param name  customer name (optional)
   * @param email customer email (optional)
   * @return list of CustomerDTO
   */
  List<CustomerDTO> searchCustomers(String name, String email);
}