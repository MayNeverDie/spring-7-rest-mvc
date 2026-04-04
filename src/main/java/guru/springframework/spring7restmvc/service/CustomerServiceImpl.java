package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        Customer firstCustomer = Customer
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("First Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer secondCustomer = Customer
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Second Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer thirdCustomer = Customer
                .builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Third Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(firstCustomer.getId(), firstCustomer);
        customerMap.put(secondCustomer.getId(), secondCustomer);
        customerMap.put(thirdCustomer.getId(), thirdCustomer);
    }

    @Override
    public List<Customer> listCustomers() {
        log.info("List customers");

        return customerMap.values().stream().toList();
    }

    @Override
    public Customer getCustomerById(UUID id) {
        log.info("Get customer by id {}: ", id);

        return customerMap.get(id);
    }

    @Override
    public Customer saveCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = customerMap.get(customerId);

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }

        customerMap.put(customerId, existingCustomer);
    }
}
