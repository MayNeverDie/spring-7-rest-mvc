package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.model.CustomerDto;
import guru.springframework.spring7restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomerControllerIntegrationTest {

    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void getCustomerById() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDto customerDto = customerController.findCustomerById(customer.getId());
        assertThat(customerDto).isNotNull();
        assertThat(customerDto.getId()).isEqualTo(customer.getId());
    }

    @Test
    public void getCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.findCustomerById(UUID.randomUUID()));
    }

    @Test
    public void listCustomers() {
        List<CustomerDto> customerDtos = customerController.listCustomers();
        assertThat(customerDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    public void listCustomersEmpty() {
        customerRepository.deleteAll();
        List<CustomerDto> customerDtos = customerController.listCustomers();
        assertThat(customerDtos.size()).isEqualTo(0);
    }

}
