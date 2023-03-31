package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findAllByIsActiveIsTrue();

    List<Category> getCategoriesByNameContainsIgnoreCaseAndIsActiveIsTrue(String filter);

    Category getCategoryByNameId(String name);

    List<Category> findCategoriesByNameIdIn(Set<String> nameIds);
}