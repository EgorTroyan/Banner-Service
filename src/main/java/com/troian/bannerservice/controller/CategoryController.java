package com.troian.bannerservice.controller;

import com.troian.bannerservice.exception.IncorrectCategoryException;
import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.repo.CategoryRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public Category addCategory(@Valid @RequestBody Category category) {
        category.setActive(true);
            categoryRepository.save(category);
        return category;
    }

    @PutMapping("/{currentCategory}")
    public Category editCategory(@PathVariable Category currentCategory,
                                 @Valid @RequestBody Category updatedCategory) {
        currentCategory.setName(updatedCategory.getName());
        currentCategory.setNameId(updatedCategory.getNameId());
        categoryRepository.save(currentCategory);
        return currentCategory;
    }

    @GetMapping
    public Set<Category> getAllCategory() {
        return categoryRepository.findAllByIsActiveIsTrue();
    }

    @GetMapping("/filter/{filter}")
    public Set<Category> getFilteredCategory(@PathVariable String filter) {
        return categoryRepository.getCategoriesByNameContainsIgnoreCaseAndIsActiveIsTrue(filter);
    }

    @GetMapping("/{category}")
    public Category getCategory(@PathVariable Category category) {
        return category;
    }

    @DeleteMapping("/{category}")
    public Category deleteCategory(@PathVariable Category category) throws IncorrectCategoryException {
        if(category.getBanners().stream().filter(Banner::isActive).collect(Collectors.toSet()).size() > 0)
            throw new IncorrectCategoryException(String.format("Category %s couldn't be deleted. Exist linked banners", category.getName()));

        category.setActive(false);
        categoryRepository.save(category);
        return category;
    }
}
