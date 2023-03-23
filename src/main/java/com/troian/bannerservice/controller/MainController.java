package com.troian.bannerservice.controller;

import com.troian.bannerservice.aop.LoggingAspect;
import com.troian.bannerservice.exception.NoBannerException;
import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.entity.Record;
import com.troian.bannerservice.model.repo.CategoryRepository;
import com.troian.bannerservice.model.repo.RecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@RestController
@RequestMapping("/bid")
public class MainController {
    private final CategoryRepository categoryRepository;
    private final RecordRepository recordRepository;

    public MainController(CategoryRepository categoryRepository, RecordRepository recordRepository) {
        this.categoryRepository = categoryRepository;
        this.recordRepository = recordRepository;
    }

    @LoggingAspect
    @GetMapping
    public Banner getBannerByCategory(@RequestParam Set<String> cat, HttpServletRequest request) throws NoBannerException{
        Set<Banner> banners = new TreeSet<>();
        cat.stream()
                .map(categoryRepository::getCategoryByNameId)
                .filter(Objects::nonNull)
                .filter(Category::isActive)
                .flatMap(category -> category.getBanners().stream())
                .filter(banner -> {
                    Record record = recordRepository.getTop1ByBannerIdOrderByDateDesc(banner.getId());
                    return record == null || !record.getDate().toLocalDate().equals(LocalDate.now());
                })
                .peek(System.out::println)
                .forEach(banners::add);

        if (banners.isEmpty()) {
            throw new NoBannerException("No banner found");
        }

        return banners.stream().findFirst().get();
    }
}
