package com.oneplusone.scheduler;

import com.oneplusone.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CrawlingScheduler {
  private final CrawlingService crawlingService;

  // 매달 1일 3시 0분 실행 (cron 형식: "0 0 3 1 * *")
  @Scheduled(cron = "0 0 3 1 * *")
  public void monthlyCrawlAndUpdate() {
    try {
      crawlingService.crawlingProductsAndUpdate();
      log.info("월간 크롤링 및 카테고리 업데이트 완료");
    } catch (Exception e) {
      log.error("크롤링 중 오류 발생", e);
    }
  }
}
