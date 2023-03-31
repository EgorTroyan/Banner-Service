package com.troian.bannerservice.aop;

import com.troian.bannerservice.exception.NoBannerException;
import com.troian.bannerservice.model.entity.Record;
import com.troian.bannerservice.model.entity.Banner;
import com.troian.bannerservice.model.entity.Category;
import com.troian.bannerservice.model.repo.RecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Aspect
@Component
public class LoggingAspectImpl {

    private final RecordRepository recordRepository;

    public LoggingAspectImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Around("@annotation(com.troian.bannerservice.aop.LoggingAspect)")
    public Object logMainRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = null;
        Record record = new Record();
        record.setDate(LocalDateTime.now());
        for(Object o : proceedingJoinPoint.getArgs()) {
            if(o instanceof HttpServletRequest)
                request = (HttpServletRequest) o;
        }
        if(request != null) {
            record.setIp(request.getRemoteHost());
            record.setAgent(request.getHeader("User-Agent"));
        }
        try {
            Banner banner = (Banner) proceedingJoinPoint.proceed();
            record.setBannerId(banner.getId());
            record.setPrice(banner.getPrice());
            banner.getCategories()
                    .stream()
                    .map(Category::getId)
                    .forEach(record.getCategoryIds()::add);
            recordRepository.save(record);
            return banner;
        } catch (NoBannerException ex) {
            record.setNoContentReason(ex.getLocalizedMessage());
            recordRepository.save(record);
            throw new NoBannerException("");
        }
    }
}
