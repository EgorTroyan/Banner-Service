package com.troian.bannerservice.service;

import com.troian.bannerservice.aop.LoggingAspect;
import com.troian.bannerservice.exception.NoBannerException;
import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.repo.BannerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BannerService {
    private final BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }
    @LoggingAspect
    public Banner getBestPriceFreshBannerByCategories(Set<String> categoryIds, HttpServletRequest request) throws NoBannerException{
        return bannerRepository.findBanner(categoryIds, request.getRemoteHost()).orElseThrow(() -> new NoBannerException("No banner found"));
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
