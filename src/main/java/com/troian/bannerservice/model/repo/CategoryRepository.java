package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Set<Category> findAllByIsActiveIsTrue();
    Set<Category> getCategoriesByNameContainsIgnoreCaseAndIsActiveIsTrue(String filter);
    Category getCategoryByNameId(String name);
}