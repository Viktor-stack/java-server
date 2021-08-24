package com.rujavacours;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class BackendSpringbootApplicationTest {

    @Autowired
    private BackendSpringbootApplication backendSpringbootApplication;

    @Test
    public void createContext() {
        assertThat(backendSpringbootApplication).isNotNull();
    }
}