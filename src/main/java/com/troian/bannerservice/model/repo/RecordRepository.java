package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long> {
    Record getTop1ByBannerIdAndIpOrderByDateDesc(long bannerId, String ip);
}