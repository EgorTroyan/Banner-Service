package com.troian.bannerservice.controller;

import com.troian.bannerservice.exception.IncorrectCategoryException;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    //private final CategoryRepository categoryRepository;

    //    @Autowired
//    public CategoryController(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category addCategory(@Valid @RequestBody Category category) {
        return categoryService.addNewCategory(category);
    }

    @PutMapping("/{currentCategory}")
    public Category editCategory(@PathVariable Category currentCategory,
                                 @Valid @RequestBody Category updatedCategory) {
        return categoryService.editCategory(currentCategory, updatedCategory);
    }

    @GetMapping
    public List<Category> getAllCategory() {
        return categoryService.getAllActiveCategory();
    }

    @GetMapping("/filter/{filter}")
    public List<Category> getFilteredCategory(@PathVariable String filter) {
        return categoryService.getAllFilteredCategory(filter);
    }

    @GetMapping("/{category}")
    public Category getCategory(@PathVariable Category category) {
        return category;
    }

    @DeleteMapping("/{category}")
    public Category deleteCategory(@PathVariable Category category) throws IncorrectCategoryException {
        return categoryService.diactivateCategory(category);
    }
}
