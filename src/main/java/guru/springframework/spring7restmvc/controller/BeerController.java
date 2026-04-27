package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.BeerDto;
import guru.springframework.spring7restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_URI = "/api/v1/beer";
    public static final String BEER_ID_URI = BEER_URI + "/{beerId}";

    private final BeerService beerService;

    @PatchMapping(BEER_ID_URI)
    public ResponseEntity<Void> patchById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDto beerDTO) {
        beerService.patchBeerById(beerId, beerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_ID_URI)
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        beerService.deleteBeerById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_ID_URI)
    public ResponseEntity<Void> updateById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDto beerDTO) {
        beerService.updateBeerById(beerId, beerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEER_URI)
    public ResponseEntity<Void> handlePost(@RequestBody BeerDto beerDTO) {
        BeerDto savedBeerDto = beerService.saveBeer(beerDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeerDto.getId());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_URI)
    public List<BeerDto> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_ID_URI)
    public BeerDto getBeerById(@PathVariable("beerId") UUID beerId) {
        log.info("Get Beer by id - in controller");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

}
