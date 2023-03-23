package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Banner;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BannerRepository extends CrudRepository<Banner, Long> {
    Set<Banner> findAllByIsActiveIsTrue();

    Set<Banner> getBannersByNameContainsIgnoreCaseAndIsActiveIsTrue(String filter);
}