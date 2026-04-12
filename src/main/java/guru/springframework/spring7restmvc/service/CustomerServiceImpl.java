package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        CustomerDTO firstCustomerDTO = CustomerDTO
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("First Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO secondCustomerDTO = CustomerDTO
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Second Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO thirdCustomerDTO = CustomerDTO
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Third Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(firstCustomerDTO.getId(), firstCustomerDTO);
        customerMap.put(secondCustomerDTO.getId(), secondCustomerDTO);
        customerMap.put(thirdCustomerDTO.getId(), thirdCustomerDTO);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        log.info("List customers");

        return customerMap.values().stream().toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.info("Get customer by id {}: ", id);

        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName(customerDTO.getCustomerName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomerDTO.getId(), savedCustomerDTO);

        return savedCustomerDTO;
    }

    @Override
    public void updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existingCustomerDTO = customerMap.get(customerId);

        existingCustomerDTO.setCustomerName(customerDTO.getCustomerName());
        existingCustomerDTO.setVersion(existingCustomerDTO.getVersion() + 1);
        existingCustomerDTO.setLastModifiedDate(LocalDateTime.now());
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existingCustomerDTO = customerMap.get(customerId);

        if (StringUtils.hasText(customerDTO.getCustomerName())) {
            existingCustomerDTO.setCustomerName(customerDTO.getCustomerName());
        }

        customerMap.put(customerId, existingCustomerDTO);
    }
}
