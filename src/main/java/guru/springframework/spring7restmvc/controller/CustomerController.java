package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.CustomerDto;
import guru.springframework.spring7restmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_URI = "/api/v1/customer";
    public static final String CUSTOMER_ID_URI = CUSTOMER_URI + "/{customerId}";

    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_ID_URI)
    public ResponseEntity<Void> patchById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDto customerDTO) {
        customerService.patchCustomerById(customerId, customerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_ID_URI)
    public ResponseEntity<Void> deleteById(@PathVariable("customerId") UUID customerId) {
        if (!customerService.deleteCustomerById(customerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_ID_URI)
    public ResponseEntity<Void> updateById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDto customerDTO) {
        customerService.updateCustomerById(customerId, customerDTO).orElseThrow(NotFoundException::new);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_URI)
    public ResponseEntity<Void> handlePost(@RequestBody CustomerDto customerDTO) {
        CustomerDto savedCustomerDto = customerService.saveCustomer(customerDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomerDto.getId());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_URI)
    public List<CustomerDto> listCustomers () {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_ID_URI)
    public CustomerDto findCustomerById (@PathVariable("customerId") UUID id) {
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

}
