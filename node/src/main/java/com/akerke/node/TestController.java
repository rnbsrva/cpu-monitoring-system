package com.akerke.node;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping
public class TestController {

    @GetMapping("/calculate")
    public Integer calculate() {
        Random random = new Random();
        Integer result = random.nextInt(100);
        for (int i = 0; i < 3000; i++) {
            Integer x = random.nextInt(100);
            result += x;
        }

        return result;
    }

}
