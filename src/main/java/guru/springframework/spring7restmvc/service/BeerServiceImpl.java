package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerDTO;
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

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO galaxyCat = BeerDTO.builder()
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

        BeerDTO crank = BeerDTO.builder()
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

        BeerDTO sunshineCity = BeerDTO.builder()
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
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.info("Get Beer by id: {}", id);

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveBeer(BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = BeerDTO.builder()
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

        beerMap.put(savedBeerDTO.getId(), savedBeerDTO);

        return savedBeerDTO;
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existingBeerDTO = beerMap.get(beerId);

        existingBeerDTO.setBeerName(beerDTO.getBeerName());
        existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existingBeerDTO.setUpc(beerDTO.getUpc());
        existingBeerDTO.setPrice(beerDTO.getPrice());
        existingBeerDTO.setUpdatedDate(LocalDateTime.now());
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existingBeerDTO = beerMap.get(beerId);

        if (StringUtils.hasText(beerDTO.getBeerName())) {
            existingBeerDTO.setBeerName(beerDTO.getBeerName());
        }
        if (StringUtils.hasText(beerDTO.getUpc())) {
            existingBeerDTO.setUpc(beerDTO.getUpc());
        }
        if (beerDTO.getBeerStyle()!=null) {
            existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        }
        if (beerDTO.getQuantityOnHand()!=null) {
            existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }
        if (beerDTO.getPrice()!=null) {
            existingBeerDTO.setPrice(beerDTO.getPrice());
        }
        beerMap.put(beerId, existingBeerDTO);
    }
}
