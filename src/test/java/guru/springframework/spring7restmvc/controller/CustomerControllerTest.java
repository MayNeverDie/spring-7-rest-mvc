package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.CustomerDto;
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
import java.util.Optional;
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
    private ArgumentCaptor<CustomerDto> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void patchCustomer() throws Exception {
        CustomerDto customerDTO = customerServiceImpl.listCustomers().getFirst();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New Name");

        /*when(customerService.updateCustomerById(any(UUID.class), any(CustomerDto.class)))
                .thenReturn(Optional.of(customerDTO));*/

        mockMvc.perform(patch(CustomerController.CUSTOMER_ID_URI, customerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customerDTO.getId());
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(customerMap.get("name"));
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDto customerDTO = customerServiceImpl.listCustomers().getFirst();

        when(customerService.deleteCustomerById(any())).thenReturn(true);

        mockMvc.perform(delete(CustomerController.CUSTOMER_ID_URI, customerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDto customerDTO = customerServiceImpl.listCustomers().getFirst();

        when(customerService.updateCustomerById(any(UUID.class), any(CustomerDto.class)))
                .thenReturn(Optional.of(customerDTO));

        mockMvc.perform(put(CustomerController.CUSTOMER_ID_URI, customerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDto.class));
    }

    @Test
    void createCustomer() throws Exception {
        CustomerDto customerDTO = customerServiceImpl.listCustomers().getFirst();
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        when(customerService.saveCustomer(any(CustomerDto.class))).thenReturn(customerServiceImpl.listCustomers().get(1));
        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
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
    void getCustomerByIdNotFound() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_ID_URI, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDto testCustomerDto = customerServiceImpl.listCustomers().getFirst();

        when(customerService.getCustomerById(testCustomerDto.getId())).thenReturn(Optional.of(testCustomerDto));

        mockMvc.perform(get(CustomerController.CUSTOMER_ID_URI, testCustomerDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCustomerDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(testCustomerDto.getName()));
    }
}