package com.example.SpringJdbcCustomer.dao;

import com.example.SpringJdbcCustomer.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    int update(Customer customer);
    int deleteById(Long id);
    List<Customer> findAll();
}