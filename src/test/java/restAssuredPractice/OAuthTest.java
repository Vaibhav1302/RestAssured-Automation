package restAssuredPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.time.Duration;


public class OAuthTest {
	
	
	@Test
	public void authTest() throws InterruptedException {
		
		//Open URL and get the code
		WebDriver driver;
		
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("identifierId")))).sendKeys("vaibhavtest19@gmail.com");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[normalize-space()='Next']")))).click();
		Thread.sleep(10000);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("Passwd")))).sendKeys("Restinpeace@123");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[normalize-space()='Next']")))).click();
		Thread.sleep(8000);
		String codeUrl = driver.getCurrentUrl();
		System.out.println(codeUrl);
		String partialcode = codeUrl.split("code=")[1];
		String Actualcode = partialcode.split("&scope")[0];
		System.out.println(Actualcode);
		driver.quit();
		
		
		//Getting Access Token
	String AccessTokenResponse = 
	     given().urlEncodingEnabled(false)
		.queryParams("code",Actualcode)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
	
	
	JsonPath js = new JsonPath(AccessTokenResponse);
	String AccessToken = js.getString("access_token");
	System.out.println("Access Token is - "+AccessToken);
		
		
		//Getting all courses response by providing access token
	String CourseList = 
			given().queryParam("access_token", AccessToken)
	       .when().get("https://rahulshettyacademy.com/getCourse.php").asString();
		    System.out.println(CourseList);
		
	}
	
	
	
	

}
