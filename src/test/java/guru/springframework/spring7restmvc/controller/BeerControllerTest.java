package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.BeerDto;
import guru.springframework.spring7restmvc.service.BeerService;
import guru.springframework.spring7restmvc.service.BeerServiceImpl;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    private BeerService beerService;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<BeerDto> beerArgumentCaptor;

    BeerServiceImpl beerServiceImpl;


    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void patchBeer() throws Exception {
        BeerDto beerDTO = beerServiceImpl.listBeers().getFirst();

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_ID_URI, beerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beerDTO.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }

    @Test
    void deleteBeer() throws Exception {
        BeerDto beerDTO = beerServiceImpl.listBeers().getFirst();

        given(beerService.deleteBeerById(beerDTO.getId())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_ID_URI, beerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeerById(captor.capture());

        assertThat(beerDTO.getId()).isEqualTo(captor.getValue());
    }

    @Test
    void updateBeer() throws Exception {
        BeerDto beerDTO = beerServiceImpl.listBeers().getFirst();

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerDTO));

        mockMvc.perform(put(BeerController.BEER_ID_URI, beerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(UUID.class), any(BeerDto.class));
    }

    @Test
    void createNewBeer() throws Exception {
        BeerDto beerDTO = beerServiceImpl.listBeers().getFirst();
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        given(beerService.saveBeer(any(BeerDto.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BeerController.BEER_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        mockMvc.perform(get(BeerController.BEER_ID_URI, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception {
        BeerDto testBeerDto = beerServiceImpl.listBeers().getFirst();

        given(beerService.getBeerById(testBeerDto.getId())).willReturn(Optional.of(testBeerDto));

        mockMvc.perform(get(BeerController.BEER_ID_URI, testBeerDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testBeerDto.getId().toString()))
                .andExpect(jsonPath("$.beerName").value(testBeerDto.getBeerName()));
    }
}