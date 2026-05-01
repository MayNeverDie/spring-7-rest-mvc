package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDto;
import guru.springframework.spring7restmvc.repositories.BeerRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BeerControllerIntegrationTest {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;

    @Test
    public void deleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    public void deleteBeerById() {
        Beer beer = beerRepository.findAll().getFirst();

        ResponseEntity<Void> responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(beerRepository.existsById(beer.getId())).isFalse();
    }

    @Test
    public void updateByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    public void updateExistingBeer() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);

        final String updatedName = "UPDATED";
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setBeerName(updatedName);

        ResponseEntity<Void> responseEntity = beerController.updateById(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(updatedName);
    }

    @Test
    public void patchByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.patchById(UUID.randomUUID(), BeerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    public void patchExistingBeer() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);

        final String updatedName = "UPDATED";
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setBeerName(updatedName);
        beerDto.setUpc("");

        ResponseEntity<Void> responseEntity = beerController.patchById(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(updatedName);
        assertThat(updatedBeer.getUpc()).isEqualTo(beer.getUpc());
    }

    @Rollback
    @Transactional
    @Test
    public void saveNewBeer() {
        BeerDto beerDto = BeerDto
                .builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<Void> response = beerController.handlePost(beerDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = response.getHeaders().getLocation().toString().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
        assertThat(beer.getBeerName()).isEqualTo("New Beer");
    }

    @Test
    public void getBeerById() {
        Beer beer = beerRepository.findAll().getFirst();

        BeerDto beerDto = beerController.getBeerById(beer.getId());

        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId()).isEqualTo(beer.getId());
    }

    @Test
    public void getBeerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    public void listBeers() {
        List<BeerDto> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    public void listBeersEmpty() {
        beerRepository.deleteAll();
        List<BeerDto> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(0);
    }

}
