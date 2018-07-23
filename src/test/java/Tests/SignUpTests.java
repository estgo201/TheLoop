package Tests;

import java.io.File;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignUpTests {
  private WebDriver driver;
  private Scanner scanner;
  
  private static JavascriptExecutor js;
  
  @Before
  public void setUp() {
    scanner = new Scanner(System.in);
    // src/test/resources/driver경로에 위치한 chromedriver 설정
    File resourcesDirectory = new File("src/test/resources/driver");
    System.setProperty("webdriver.chrome.driver", resourcesDirectory + "/chromedriver");
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;  // Web Driver를 JavascriptExecutor로 캐스팅
  }
  
  @After
  public void tearDown() {
    driver.quit();
  }
  
  /*
   * 계정 이름을 입력받아서 twitter.com에서 그에 해당하는 새 계정을 만드는 테스트 자동화 코드를 작성하세요.  
   */
  @Test
  public void test() {
    // step1. twitter에 새 계정 만들기
    String baseUrl = "https://twitter.com/signup";
    driver.get(baseUrl);
    String inputValue;
    
    WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));
    
    // step2. user에게 이름, 전화번호를 입력받아 form에 입력
    retryingFindClick(By.name("name"));
    inputValue = requestInputfromUser("이름");
    driver.findElement(By.name("name")).sendKeys(inputValue);
    inputValue = requestInputfromUser("전화번호");
    driver.findElement(By.name("phone_number")).sendKeys(inputValue);

    By nextXPath = By.xpath("//div[contains(@class,'rn-1oszu61 rn-urgr8i rn-1c1gj4h rn-114ovsg rn-oucylx rn-855088 rn-1bxrh7q rn-waaub4 rn-sqtsar rn-qb5c1y rn-1efd50x rn-14skgim rn-rull8r rn-mm0ijv rn-5kkj8d rn-13l2t4g rn-qklmqi rn-1ljd8xs rn-deolkf rn-1loqt21 rn-6koalj rn-1qe8dj5 rn-1mlwlqe rn-eqz5dr rn-1w2pmg rn-1mnahxq rn-61z16t rn-p1pxzi rn-11wrixw rn-1eg1yxq rn-qb0742 rn-wk8lta rn-1pl8ijs rn-1mdbw0j rn-1wx0zj rn-bnwqim rn-o7ynqc rn-zca6y rn-lrvibr rn-1lgpqti')]");
    By registerXPath = By.xpath("//div[contains(@class,'rn-1oszu61 rn-urgr8i rn-1c1gj4h rn-114ovsg rn-oucylx rn-855088 rn-1bxrh7q rn-waaub4 rn-sqtsar rn-qb5c1y rn-1efd50x rn-14skgim rn-rull8r rn-mm0ijv rn-5kkj8d rn-13l2t4g rn-qklmqi rn-1ljd8xs rn-deolkf rn-1loqt21 rn-6koalj rn-1qe8dj5 rn-1mlwlqe rn-eqz5dr rn-1w2pmg rn-61z16t rn-p1pxzi rn-11wrixw rn-m9gq36 rn-it97yl rn-1f3jsut rn-wk8lta rn-1pl8ijs rn-1mdbw0j rn-1wx0zj rn-bnwqim rn-o7ynqc rn-zca6y rn-lrvibr rn-1lgpqti')]");
    By confirmXPath = By.xpath("//div[contains(@class,'rn-1oszu61 rn-urgr8i rn-1c1gj4h rn-114ovsg rn-oucylx rn-855088 rn-1bxrh7q rn-waaub4 rn-sqtsar rn-qb5c1y rn-1efd50x rn-14skgim rn-rull8r rn-mm0ijv rn-5kkj8d rn-13l2t4g rn-qklmqi rn-1ljd8xs rn-deolkf rn-1loqt21 rn-6koalj rn-1qe8dj5 rn-1mlwlqe rn-eqz5dr rn-1w2pmg rn-61z16t rn-p1pxzi rn-11wrixw rn-8rf56t rn-it97yl rn-1f3jsut rn-wk8lta rn-1pl8ijs rn-1mdbw0j rn-1wx0zj rn-bnwqim rn-o7ynqc rn-zca6y rn-lrvibr rn-1lgpqti')]");
    
    // step3. 입력받은 정보로 가입시작
    waitUntilClickableAndClick(nextXPath);
    waitUntilClickableAndClick(registerXPath);
    waitUntilClickableAndClick(confirmXPath);
    
    // stpe4. 인증번호 입력요청 후 click
    inputValue = requestInputfromUser("확인코드");
    driver.findElement(By.xpath("//*[@id=\"react-root\"]/div[2]/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[3]/div/div/input")).sendKeys(inputValue);
    waitUntilClickableAndClick(nextXPath);
    
    // step5. 비밀번호 입력요청 후 click
    inputValue = requestInputfromUser("비밀번호(6자리)");
    driver.findElement(By.name("password")).sendKeys(inputValue);
    waitUntilClickableAndClick(nextXPath);
  }
  
  private String requestInputfromUser(String askTitle) {
    System.out.print(askTitle + " : ");
    String inputValue = scanner.nextLine();
    System.out.println(inputValue);
    return inputValue;
  }
  
  private void waitUntilClickableAndClick(By by) {
    WebDriverWait wait = new WebDriverWait(driver, 10);
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
    
    element.click();
  }
  
  private boolean retryingFindClick(By by) {
    boolean result = false;
    int attempts = 0;
    while(attempts < 2) {
        try {
            driver.findElement(by).click();
            result = true;
            break;
        } catch(StaleElementReferenceException e) {
          System.out.println("StaleElementReferenceException occurred attempts count : " + attempts);
        }
        attempts++;
    }
    return result;
  }
}
