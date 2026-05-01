package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDto;
import guru.springframework.spring7restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDto> listBeers() {
        return beerRepository
                .findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .toList();
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDto saveBeer(BeerDto beerDTO) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
    }

    @Override
    public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beerDTO) {
        return beerRepository.findById(beerId).map(foundBeer -> {
            foundBeer.setBeerName(beerDTO.getBeerName());
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            foundBeer.setUpc(beerDTO.getUpc());
            foundBeer.setPrice(beerDTO.getPrice());
            foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            return beerMapper.beerToBeerDto(beerRepository.save(foundBeer));
        });
    }

    @Override
    public boolean deleteBeerById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beerDTO) {
        return beerRepository.findById(beerId).map((foundBeer) -> {
            if (StringUtils.hasText(beerDTO.getBeerName())) {
                foundBeer.setBeerName(beerDTO.getBeerName());
            }
            if (StringUtils.hasText(beerDTO.getUpc())) {
                foundBeer.setUpc(beerDTO.getUpc());
            }
            if (beerDTO.getBeerStyle()!=null) {
                foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            }
            if (beerDTO.getQuantityOnHand()!=null) {
                foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            }
            if (beerDTO.getPrice()!=null) {
                foundBeer.setPrice(beerDTO.getPrice());
            }
            return beerMapper.beerToBeerDto(beerRepository.save(foundBeer));
        });
    }
}
