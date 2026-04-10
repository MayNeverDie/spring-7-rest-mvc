package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.Customer;
import guru.springframework.spring7restmvc.service.CustomerService;
import guru.springframework.spring7restmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CustomerService customerService;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void patchCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_ID_URI, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(customerMap.get("customerName"));
    }

    @Test
    void deleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();

        mockMvc.perform(delete(CustomerController.CUSTOMER_ID_URI, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();

        mockMvc.perform(put(CustomerController.CUSTOMER_ID_URI, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(any(UUID.class), any(Customer.class));
    }

    @Test
    void createCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();
        customer.setId(null);
        customer.setVersion(null);

        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customerServiceImpl.listCustomers().get(1));
        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listCustomers() throws Exception {
        when(customerService.listCustomers()).thenReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void findCustomerById() throws Exception {
        Customer testCustomer = customerServiceImpl.listCustomers().getFirst();

        when(customerService.getCustomerById(testCustomer.getId())).thenReturn(testCustomer);

        mockMvc.perform(get(CustomerController.CUSTOMER_ID_URI, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()))
                .andExpect(jsonPath("$.customerName").value(testCustomer.getCustomerName()));
    }
}