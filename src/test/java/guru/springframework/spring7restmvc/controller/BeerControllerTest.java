package guru.springframework.spring7restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class BeerControllerTest {

    @Autowired
    private BeerController controller;

    @Test
    void getBeerById() {
        controller.getBeerById(UUID.randomUUID());
    }
}