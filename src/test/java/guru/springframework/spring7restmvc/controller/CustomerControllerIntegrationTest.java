package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.mappers.CustomerMapper;
import guru.springframework.spring7restmvc.model.CustomerDto;
import guru.springframework.spring7restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomerControllerIntegrationTest {

    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;

    @Test
    public void patchExistingCustomerNotFound() {
        CustomerDto customerDto = CustomerDto.builder().build();
        assertThrows(NotFoundException.class, ()-> customerController.patchById(UUID.randomUUID(), customerDto));
    }

    @Transactional
    @Rollback
    @Test
    public void patchExistingCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDto customerDto = customerMapper.customerToCustomerDto(customer);

        final String updatedName =  "UPDATED";
        customerDto.setId(null);
        customerDto.setVersion(null);
        customerDto.setName(updatedName);

        ResponseEntity<Void> responseEntity = customerController.patchById(customer.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(updatedName);
    }

    @Test
    public void deleteCustomerByIdNotFound() {
       assertThrows(NotFoundException.class, () -> customerController.deleteById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    public void deleteCustomerById() {
        Customer customer = customerRepository.findAll().getFirst();

        ResponseEntity<Void> responseEntity = customerController.deleteById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertFalse(customerRepository.existsById(customer.getId()));
    }

    @Test
    public void updateExistingCustomerNotFound() {
        CustomerDto customerDto = CustomerDto.builder().build();
        assertThrows(NotFoundException.class, ()-> customerController.updateById(UUID.randomUUID(), customerDto));
    }

    @Transactional
    @Rollback
    @Test
    public void updateExistingCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDto customerDto = customerMapper.customerToCustomerDto(customer);

        final String updatedName =  "UPDATED";
        customerDto.setId(null);
        customerDto.setVersion(null);
        customerDto.setName(updatedName);

        ResponseEntity<Void> responseEntity = customerController.updateById(customer.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(updatedName);
    }

    @Transactional
    @Rollback
    @Test
    public void saveCustomer() {
        CustomerDto customerDto = CustomerDto
                .builder()
                .name("New Customer Name")
                .build();

        ResponseEntity<Void> responseEntity = customerController.handlePost(customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().toString().split("/");
        UUID customerId = UUID.fromString(location[4]);

        Customer customer = customerRepository.findById(customerId).get();
        assertThat(customer).isNotNull();
        assertThat(customer.getName()).isEqualTo(customerDto.getName());
    }

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
