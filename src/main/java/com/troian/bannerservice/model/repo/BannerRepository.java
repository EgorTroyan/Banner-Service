package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Banner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BannerRepository extends CrudRepository<Banner, Long> {
    List<Banner> findAllByIsActiveIsTrue();

    List<Banner> getBannersByNameContainsIgnoreCaseAndIsActiveIsTrue(String filter);
    @Query(value = "SELECT DISTINCT b.id, b.is_active, b.name, b.price, b.text" +
            " FROM banner b " +
            "         JOIN category_banner cb on b.id = cb.banner_id " +
            "         JOIN (SELECT id, is_active, name, name_id " +
            "               FROM category\n" +
            "               WHERE name_id IN (:categories)) c on cb.category_id = c.id " +
            "WHERE b.id NOT IN (SELECT banner_id " +
            "                   FROM record " +
            "                   WHERE date(date) = CURDATE() " +
            "                   AND ip_adress = :ip) " +
            "ORDER BY b.price DESC LIMIT 1",
    nativeQuery = true)
    Optional<Banner> findBanner(@Param("categories") Set<String> categories, @Param("ip") String ip);
}

//    SELECT b.id, b.is_active, b.name, b.price, b.text
//        FROM banner b
//        JOIN category_banner cb on b.id = cb.banner_id
//        JOIN (SELECT id, is_active, name, name_id
//        FROM category
//        WHERE name_id IN ('a', 'b')) c on cb.category_id = c.id
//        WHERE b.id NOT IN (SELECT banner_id
//        FROM record
//WHERE date(date) = CURDATE())
//        ORDER BY b.price DESC;