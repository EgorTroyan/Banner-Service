package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Banner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BannerRepository extends CrudRepository<Banner, Long> {
    List<Banner> findAllByIsActiveIsTrue();

    List<Banner> getBannersByNameContainsIgnoreCaseAndIsActiveIsTrue(String filter);
}