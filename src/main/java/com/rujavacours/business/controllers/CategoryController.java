package com.rujavacours.business.controllers;

import com.rujavacours.business.DTO.CategoryDTO;
import com.rujavacours.business.entity.Category;
import com.rujavacours.business.service.CategoryService;
import com.rujavacours.business.util.MyLogger;
import com.rujavacours.business.util.ValidationStringAndId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/all")
    public List<Category> findAll(@RequestBody CategoryDTO categoryDTO) {
        MyLogger.debugMethodName("CategoryController FindAll----------------------------------------------------->");
        return categoryService.findAll(categoryDTO);
    }


    @PutMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        MyLogger.debugMethodName("CategoryController add()------------------------------------------------------->");
        if (ValidationStringAndId.isNoValidId(category.getId())) {
            return new ResponseEntity("redundant param: is MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (ValidationStringAndId.isValidString(category.getTitle())) {
            return new ResponseEntity("The title must not be empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(categoryService.addOrUpdate(category));
    }

    @PatchMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {
        MyLogger.debugMethodName("CategoryController update(Category)---------------------------------------------------->");
        if (ValidationStringAndId.isValidId(category.getId())) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (ValidationStringAndId.isValidString(category.getTitle())) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryService.addOrUpdate(category);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // удоление обекта
    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long id) {
        MyLogger.debugMethodName("CategoryController delete(id)---------------------------------------------------->");
        if (id == null || id == 0) {
            return new ResponseEntity("missed params ID", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + "not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // поиск обектов по названию Title
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategoryDTO categoryDTO) {
        MyLogger.debugMethodName("Category  search()------------------------------------------------------------->");
        List<Category> list = categoryService.search(categoryDTO.getTitle(), categoryDTO.getEmail());
        return ResponseEntity.ok(list);
    }

    // поиск обекта по ID
    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        MyLogger.debugMethodName("Category findById(id)----------------------------------------------------------->");
        Category category = null;
        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + "not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

}
