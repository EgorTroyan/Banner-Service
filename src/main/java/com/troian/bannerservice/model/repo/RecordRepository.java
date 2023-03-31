package com.troian.bannerservice.model.repo;

import com.troian.bannerservice.model.entity.Record;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends CrudRepository<Record, Long> {
    Record getTop1ByBannerIdAndIpOrderByDateDesc(long bannerId, String ip);
    List<Record> findRecordsByIpAndDateAfter(String ip, LocalDateTime date);
}