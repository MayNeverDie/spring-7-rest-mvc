package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.mappers.CustomerMapper;
import guru.springframework.spring7restmvc.model.CustomerDto;
import guru.springframework.spring7restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> listCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .toList();
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        return Optional
                .ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDTO) {
        Customer newCustomer = customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO));
        return customerMapper.customerToCustomerDto(newCustomer);
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID customerId, CustomerDto customerDTO) {
        return customerRepository.findById(customerId).map(foundCustomer -> {
            foundCustomer.setName(customerDTO.getName());
            foundCustomer.setLastModifiedDate(customerDTO.getLastModifiedDate());
            return customerMapper.customerToCustomerDto(customerRepository.save(foundCustomer));
        });
    }

    @Override
    public boolean deleteCustomerById(UUID customerId) {
        if(customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID customerId, CustomerDto customerDTO) {
        return customerRepository.findById(customerId).map((existingCustomer) -> {
            if (StringUtils.hasText(customerDTO.getName())) {
                existingCustomer.setName(customerDTO.getName());
            }
            return customerMapper.customerToCustomerDto(customerRepository.save(existingCustomer));
        });
    }
}
