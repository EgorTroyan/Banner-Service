package com.troian.bannerservice.controller;

import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.repo.BannerRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final BannerRepository bannerRepository;

    @Autowired
    public BannerController(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @PostMapping
    public Banner addBanner(@Valid @RequestBody Banner banner, @RequestParam Set<Category> cat) {
        banner.setActive(true);
        cat.forEach(banner::addCategoryToBanner);
        return bannerRepository.save(banner);
    }

    @PutMapping("/{currentBanner}")
    public Banner editBanner(@PathVariable Banner currentBanner,
                                 @Valid @RequestBody Banner updatedBanner) {
        currentBanner.setName(updatedBanner.getName());
        currentBanner.setText(updatedBanner.getText());
        currentBanner.setPrice(updatedBanner.getPrice());
        currentBanner.setActive(updatedBanner.isActive());
        currentBanner.setCategories(updatedBanner.getCategories());

        return bannerRepository.save(currentBanner);
    }

    @GetMapping
    public Set<Banner> getAllBanner() {
        return new TreeSet<>(bannerRepository.findAllByIsActiveIsTrue());
    }

    @GetMapping("/filter/{filter}")
    public Set<Banner> getFilteredBanner(@PathVariable String filter) {
        return new TreeSet<>(bannerRepository.getBannersByNameContainsIgnoreCaseAndIsActiveIsTrue(filter));
    }

    @GetMapping("/{banner}")
    public Banner getBanner(@PathVariable Banner banner) {
        return banner;
    }

    @DeleteMapping("/{banner}")
    public Banner deleteBanner(@PathVariable Banner banner) {
        banner.setActive(false);
        return bannerRepository.save(banner);
    }
}
