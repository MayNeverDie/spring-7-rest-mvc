package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        Customer firstCustomer = Customer
                .builder()
                .id(UUID.randomUUID())
                .customerName("First Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer secondCustomer = Customer
                .builder()
                .id(UUID.randomUUID())
                .customerName("Second Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer thirdCustomer = Customer
                .builder()
                .id(UUID.randomUUID())
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
}
