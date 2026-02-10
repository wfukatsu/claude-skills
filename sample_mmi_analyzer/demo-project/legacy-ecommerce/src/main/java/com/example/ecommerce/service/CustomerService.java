package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Customer service
 * Problem: Missing validation logic
 * Problem: Email format not validated
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {
        // Problem: No validation of email format
        // Problem: No check for duplicate email
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerData) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Problem: No validation of changes
        customer.setName(customerData.getName());
        customer.setEmail(customerData.getEmail());
        customer.setPhone(customerData.getPhone());
        customer.setAddress(customerData.getAddress());
        customer.setCity(customerData.getCity());
        customer.setState(customerData.getState());
        customer.setZipCode(customerData.getZipCode());

        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
