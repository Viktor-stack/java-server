package com.rujavacours.business.controllers;

import com.rujavacours.business.entity.Category;
import com.rujavacours.business.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class CategoryControllerTest {

    private final CategoryService categoryService;

    @Autowired
    public CategoryControllerTest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Test
    public void findAll() {
        Category category = new Category();
        assertThat(category).isNotNull();
    }
}