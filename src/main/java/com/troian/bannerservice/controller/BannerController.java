package com.troian.bannerservice.controller;

import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.service.BannerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/banner")
public class BannerController {
    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PostMapping
    public Banner addBanner(@Valid @RequestBody Banner banner, @RequestParam List<Category> cat) {
        return bannerService.addNewBanner(banner, cat);
    }

    @PutMapping("/{currentBanner}")
    public Banner editBanner(@PathVariable Banner currentBanner,
                             @Valid @RequestBody Banner updatedBanner) {
        return bannerService.editBanner(currentBanner, updatedBanner);
    }

    @GetMapping
    public List<Banner> getAllBanner() {
        return bannerService.getAllActiveBanners();
    }

    @GetMapping("/filter/{filter}")
    public List<Banner> getFilteredBanner(@PathVariable String filter) {
        return bannerService.getAllFilteredBanners(filter);
    }

    @GetMapping("/{banner}")
    public Banner getBanner(@PathVariable Banner banner) {
        return banner;
    }

    @DeleteMapping("/{banner}")
    public Banner deleteBanner(@PathVariable Banner banner) {
        return bannerService.disactivateBanner(banner);
    }
}
