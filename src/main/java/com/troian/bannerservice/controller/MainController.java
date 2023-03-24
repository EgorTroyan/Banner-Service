package com.troian.bannerservice.controller;

import com.troian.bannerservice.exception.NoBannerException;
import com.troian.bannerservice.service.BannerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/bid")
public class MainController {
    private final BannerService bannerService;

    public MainController(BannerService bannerService) {
        this.bannerService = bannerService;
    }


    @GetMapping
    public String getBannerByCategory(@RequestParam Set<String> cat, HttpServletRequest request) throws NoBannerException {
        return bannerService.getBestPriceFreshBannerByCategories(cat, request).getText();
    }
}
