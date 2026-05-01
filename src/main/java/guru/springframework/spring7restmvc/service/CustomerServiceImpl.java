package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDto> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        CustomerDto firstCustomerDto = CustomerDto
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("First Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDto secondCustomerDto = CustomerDto
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Second Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDto thirdCustomerDto = CustomerDto
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Third Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(firstCustomerDto.getId(), firstCustomerDto);
        customerMap.put(secondCustomerDto.getId(), secondCustomerDto);
        customerMap.put(thirdCustomerDto.getId(), thirdCustomerDto);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        log.info("List customers");

        return customerMap.values().stream().toList();
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        log.info("Get customer by id {}: ", id);

        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDTO) {

        CustomerDto savedCustomerDto = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name(customerDTO.getName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomerDto.getId(), savedCustomerDto);

        return savedCustomerDto;
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID customerId, CustomerDto customerDTO) {
        CustomerDto existingCustomerDto = customerMap.get(customerId);

        existingCustomerDto.setName(customerDTO.getName());
        existingCustomerDto.setVersion(existingCustomerDto.getVersion() + 1);
        existingCustomerDto.setLastModifiedDate(LocalDateTime.now());

        return Optional.of(existingCustomerDto);
    }

    @Override
    public boolean deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDto customerDTO) {
        CustomerDto existingCustomerDto = customerMap.get(customerId);

        if (StringUtils.hasText(customerDTO.getName())) {
            existingCustomerDto.setName(customerDTO.getName());
        }

        customerMap.put(customerId, existingCustomerDto);
    }
}
