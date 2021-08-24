package com.rujavacours.business.service;


import com.rujavacours.business.DTO.CategoryDTO;
import com.rujavacours.business.entity.Category;
import com.rujavacours.business.repo.CategoryRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepos categoryRepos;

    @Autowired
    public CategoryService(CategoryRepos categoryRepos) {
        this.categoryRepos = categoryRepos;
    }

    public List<Category> findAll(CategoryDTO categoryDTO) {
        return categoryRepos.findByUserEmailOrderByTitleAsc(categoryDTO.getEmail());
    }


    public Category addOrUpdate(Category category) {
        return categoryRepos.save(category);
    }

    public void deleteById(Long id) {
        categoryRepos.deleteById(id);
    }

    public List<Category> search(String email, String title) {
        return categoryRepos.search(email, title);
    }

    public Category findById(Long id) {
        return categoryRepos.findById(id).get();
    }
}
