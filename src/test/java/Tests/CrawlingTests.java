package Tests;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Data.BlogInfo;

public class CrawlingTests {
  private WebDriver driver;
  
  private static String[] columns = {"title", "url"};
  private static List<BlogInfo> blogInfoList =  new ArrayList<>();
  
  @Before
  public void setUp() {
    // src/test/resources/driver경로에 위치한 chromedriver 설정
    File resourcesDirectory = new File("src/test/resources/driver");
    System.setProperty("webdriver.chrome.driver", resourcesDirectory + "/chromedriver");
    driver = new ChromeDriver();
  }
  
  @After
  public void tearDown() {
    driver.quit();
    blogInfoList.clear();
  }
  
  /*
   * Naver 검색창에서 ICON foundation으로 검색을 하고, 결과 중 가장 최신에 작성된 3개의 블로그 주소를 엑셀로 저장하는 테스트 자동화 코드를 작성하세요.
   */
  @Test(timeout=10000L)
  public void test() throws Exception {
    String baseUrl = "http://www.naver.com";
    String query = "ICON foundation";

    // step1. Targer url로 접속 및 query string으로 검색
    driver.get(baseUrl);
    driver.findElement(By.id("query")).sendKeys(query);
    driver.findElement(By.id("search_btn")).click();
    
    // 검색결과가 나오기까지 explicit wait
    WebDriverWait wait = new WebDriverWait(driver, 1);
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"main_pack\"]/div[3]/div[1]/h2")));
 
    // step2. Web Crawling TODO Jsoup과 비교 필요
    // blog section이 존재하면 li list들을 search
    List<WebElement> blogLists = driver.findElement(By.xpath("//*[@id=\"main_pack\"]/div[3]/ul")).findElements(By.tagName("li"));

    for (int i = 0; i < blogLists.size(); i++){
      WebElement element = blogLists.get(i);
      String title = element.findElement(By.xpath("dl/dt/a")).getAttribute("title");
      String url = element.findElement(By.xpath("dl/dt/a")).getAttribute("href");
      
      blogInfoList.add(new BlogInfo(title, url));
    }

    // step3. excel에 저장, path : src/test/resources/results/blogInfo.xlsx
    File resourcesDirectory = new File("src/test/resources/results");
    writeToExcel(resourcesDirectory+"/blogInfo.xlsx");
  }
  
  // 전달 받은 location의 excel file에 crawling한 데이터 저장
  @SuppressWarnings("resource")
  private void writeToExcel(String location) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Blog");
    
    Row row;
    row = sheet.createRow(0);
    row.createCell(0).setCellValue(columns[0]);
    row.createCell(1).setCellValue(columns[1]);      
    
    for(int i = 0; i < 3; i++) {
      row = sheet.createRow(i + 1);
      row.createCell(0).setCellValue(blogInfoList.get(i).getTitle());
      row.createCell(1).setCellValue(blogInfoList.get(i).getUrl());
    }
    
    for(int i = 0; i < columns.length; i++) {
      sheet.autoSizeColumn(i);
    }

    // Write the output to a file
    FileOutputStream fileOut = new FileOutputStream(location);
    workbook.write(fileOut);
    fileOut.close();
  }
  
}
