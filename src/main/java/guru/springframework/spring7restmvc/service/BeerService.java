package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDto> listBeers();

    Optional<BeerDto> getBeerById(UUID id);

    BeerDto saveBeer(BeerDto beerDTO);

    void updateBeerById(UUID beerId, BeerDto beerDTO);

    void deleteBeerById(UUID beerId);

    void patchBeerById(UUID beerId, BeerDto beerDTO);
}
