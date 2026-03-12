package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    public List<Customer> listCustomers();

    public Customer getCustomerById(UUID id);

}
