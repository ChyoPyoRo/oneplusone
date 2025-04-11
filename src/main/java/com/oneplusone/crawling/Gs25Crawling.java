package com.oneplusone.crawling;

import com.oneplusone.dtos.ProductDto;
import com.oneplusone.enums.ConvenienceName;
import com.oneplusone.enums.EventType;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

@Component
public class Gs25Crawling {
  public static List<ProductDto> crawlByEachEvent() throws InterruptedException {//행사별로 상품 크롤링
    List<ProductDto> oneToOneProductList = crawlProduct(EventType.ONE_TO_ONE);
    List<ProductDto> twoToOneProductList = crawlProduct(EventType.TWO_TO_ONE);
    oneToOneProductList.addAll(twoToOneProductList);
    return oneToOneProductList;
  }
  private static List<ProductDto> crawlProduct(EventType eventType) throws InterruptedException {//상품 크롤링
    WebDriverManager.chromedriver().setup();//chromeDriver 버전 확인
    WebDriver driver = new ChromeDriver();//웹 driver 생성
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));//최대 10초로 wait 설정
    driver.get("http://gs25.gsretail.com/gscvs/ko/products/event-goods");//홈페이지 이동
    WebElement tab = driver.findElement(By.id(eventType.getHTMLName()));//event 탬 요소 찾기
    tab.click();//클릭
    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("blockUI")));//로딩 창 기다리기
    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("blockUI")));
    Thread.sleep(1000);//1초 딜레이(차단 방지)
    List<ProductDto> result = new ArrayList<>();
    while (true) {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prod_list li")));
      // 상품명 출력
      List<WebElement> tables = driver.findElements(By.cssSelector(".tblwrap.mt50"));
      for (WebElement table : tables) {
        if (!table.isDisplayed()) continue;//display가 아닌 부분은 조회X
        List<WebElement> items = table.findElements(By.cssSelector(".prod_list li"));
        for (WebElement item : items) {//display가 아닌 부분은 조회X
          if (!item.isDisplayed()) continue;
          List<WebElement> titleList = item.findElements(By.cssSelector(".tit"));//상품명
          List<WebElement> priceList = item.findElements(By.cssSelector(".cost"));//가격
          if (!titleList.isEmpty() && !priceList.isEmpty()) {
            try {
              String name = titleList.get(0).getText().trim();
              String priceText = priceList.get(0).getText().replaceAll("[^\\d]", "");
              Long price = priceText.isEmpty() ? null : Long.parseLong(priceText);//String에서 Long으로 파싱
              //List에 데이터 추가
              if (!name.isEmpty() && price != null) result.add(new ProductDto(name, price, eventType, ConvenienceName.GS25));
            } catch (Exception e) {
              driver.quit();//에러 발생시에는 그냥 종료 시킴
              System.out.println("상품명 추출 실패: " + e.getMessage());
            }
          }
        }
      }
      List<WebElement> nextButtons = driver.findElements(By.cssSelector("div.paging a.next"));
      boolean clicked = false;
      for (WebElement nextButton : nextButtons) {
        String onclick = nextButton.getAttribute("onclick");
        boolean visible = nextButton.isDisplayed();
        if (onclick != null && onclick.contains("moveControl") && visible) {
          nextButton.click();
          wait.until(ExpectedConditions.presenceOfElementLocated(By.className("blockUI")));
          wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("blockUI")));
          clicked = true;
          break;
        }
      }
      Thread.sleep(1000);//1초동안 기다리기 (차단 방지)
      if (!clicked) break;//click이 눌리지 않았으면 루프 종료
    }
    driver.quit(); // 종료는 필요 시 사용
    return new ArrayList<>();
  }
}

