package com.troian.bannerservice.service;

import com.troian.bannerservice.exception.IncorrectCategoryException;
import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.repo.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addNewCategory(Category category) {
        category.setActive(true);
        categoryRepository.save(category);
        return category;
    }

    public Category editCategory(Category currentCategory, Category updatedCategory) {
        currentCategory.setName(updatedCategory.getName());
        currentCategory.setNameId(updatedCategory.getNameId());
        categoryRepository.save(currentCategory);
        return currentCategory;
    }

    public List<Category> getAllActiveCategory() {
        return categoryRepository.findAllByIsActiveIsTrue();
    }

    public List<Category> getAllFilteredCategory(String filter) {
        return categoryRepository.getCategoriesByNameContainsIgnoreCaseAndIsActiveIsTrue(filter);
    }

    public Category diactivateCategory(Category category) throws IncorrectCategoryException {
        System.out.println(category.getBanners());
        if (category.getBanners().stream().filter(Banner::isActive).toList().size() > 0)
            throw new IncorrectCategoryException(String.format("Category %s couldn't be deleted. Exist linked banners", category.getName()));

        category.setActive(false);
        categoryRepository.save(category);
        return category;
    }
}
