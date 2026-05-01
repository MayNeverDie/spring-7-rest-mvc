package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDto> listCustomers();

    Optional<CustomerDto> getCustomerById(UUID id);

    CustomerDto saveCustomer(CustomerDto customerDTO);

    Optional<CustomerDto> updateCustomerById(UUID customerId, CustomerDto customerDTO);

    boolean deleteCustomerById(UUID customerId);

    Optional<CustomerDto> patchCustomerById(UUID customerId, CustomerDto customerDTO);
}
