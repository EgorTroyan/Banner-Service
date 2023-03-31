package com.troian.bannerservice.service;

import com.troian.bannerservice.aop.LoggingAspect;
import com.troian.bannerservice.exception.NoBannerException;
import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.entity.Record;
import com.troian.bannerservice.model.repo.BannerRepository;
import com.troian.bannerservice.model.repo.CategoryRepository;
import com.troian.bannerservice.model.repo.RecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BannerService {
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final RecordRepository recordRepository;

    public BannerService(BannerRepository bannerRepository, CategoryRepository categoryRepository, RecordRepository recordRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.recordRepository = recordRepository;
    }
    @LoggingAspect
    public Banner getBestPriceFreshBannerByCategories(Set<String> categoryIds, HttpServletRequest request) throws NoBannerException{
        List<Banner> banners = new ArrayList<>();
        List<Record> todayRecordsWithIp =
                recordRepository.findRecordsByIpAndDateAfter(request.getRemoteHost(), LocalDate.now().atStartOfDay()) == null
                        ? new ArrayList<>()
                        : recordRepository.findRecordsByIpAndDateAfter(request.getRemoteHost(), LocalDate.now().atStartOfDay());

        List<Category> categories = categoryRepository.findCategoriesByNameIdIn(categoryIds);
        if(categories != null) {
            categories.stream()
                    .filter(Category::isActive)
                    .flatMap(category -> category.getBanners().stream())
                    .filter(banner -> todayRecordsWithIp.stream().noneMatch(r -> r.getBannerId() == banner.getId()))
                    .forEach(banners::add);
        }
//        categoryIds.stream()
//                .map(categoryRepository::getCategoryByNameId)
//                .filter(Objects::nonNull)
//                .filter(Category::isActive)
//                .flatMap(category -> category.getBanners().stream())
//                .filter(banner -> {
//                    Record record = recordRepository.getTop1ByBannerIdAndIpOrderByDateDesc(banner.getId(), request.getRemoteHost());
//                    return record == null || !record.getDate().toLocalDate().equals(LocalDate.now());
//                })
//                .forEach(banners::add);

        if (banners.isEmpty()) {
            throw new NoBannerException("No banner found");
        }

        return banners.stream()
                .sorted()
                .findFirst()
                .get();
    }

    public Banner addNewBanner(Banner banner, List<Category> cat) {
        banner.setActive(true);
        cat.forEach(banner::addCategoryToBanner);

        return bannerRepository.save(banner);
    }

    public Banner editBanner(Banner currentBanner, Banner updatedBanner) {
        currentBanner.setName(updatedBanner.getName());
        currentBanner.setText(updatedBanner.getText());
        currentBanner.setPrice(updatedBanner.getPrice());
        currentBanner.setActive(updatedBanner.isActive());
        currentBanner.setCategories(updatedBanner.getCategories());

        return bannerRepository.save(currentBanner);
    }

    public List<Banner> getAllActiveBanners() {
        return bannerRepository.findAllByIsActiveIsTrue()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Banner> getAllFilteredBanners(String filter) {
        return bannerRepository.getBannersByNameContainsIgnoreCaseAndIsActiveIsTrue(filter)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Banner disactivateBanner(Banner banner) {
        banner.setActive(false);
        return bannerRepository.save(banner);
    }
}
