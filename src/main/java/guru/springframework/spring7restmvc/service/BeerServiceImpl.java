package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerDto;
import guru.springframework.spring7restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDto> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDto galaxyCat = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12345")
                .price(new BigDecimal("12"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDto crank = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12334522")
                .price(new BigDecimal("11"))
                .quantityOnHand(400)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDto sunshineCity = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12344111")
                .price(new BigDecimal("14"))
                .quantityOnHand(150)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(galaxyCat.getId(), galaxyCat);
        beerMap.put(crank.getId(), crank);
        beerMap.put(sunshineCity.getId(), sunshineCity);
    }

    @Override
    public List<BeerDto> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        log.info("Get Beer by id: {}", id);

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDto saveBeer(BeerDto beerDTO) {
        BeerDto savedBeerDto = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .build();

        beerMap.put(savedBeerDto.getId(), savedBeerDto);

        return savedBeerDto;
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDto beerDTO) {
        BeerDto existingBeerDto = beerMap.get(beerId);

        existingBeerDto.setBeerName(beerDTO.getBeerName());
        existingBeerDto.setBeerStyle(beerDTO.getBeerStyle());
        existingBeerDto.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existingBeerDto.setUpc(beerDTO.getUpc());
        existingBeerDto.setPrice(beerDTO.getPrice());
        existingBeerDto.setUpdatedDate(LocalDateTime.now());
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDto beerDTO) {
        BeerDto existingBeerDto = beerMap.get(beerId);

        if (StringUtils.hasText(beerDTO.getBeerName())) {
            existingBeerDto.setBeerName(beerDTO.getBeerName());
        }
        if (StringUtils.hasText(beerDTO.getUpc())) {
            existingBeerDto.setUpc(beerDTO.getUpc());
        }
        if (beerDTO.getBeerStyle()!=null) {
            existingBeerDto.setBeerStyle(beerDTO.getBeerStyle());
        }
        if (beerDTO.getQuantityOnHand()!=null) {
            existingBeerDto.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }
        if (beerDTO.getPrice()!=null) {
            existingBeerDto.setPrice(beerDTO.getPrice());
        }
        beerMap.put(beerId, existingBeerDto);
    }
}
