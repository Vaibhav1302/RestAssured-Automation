package restAssuredPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonParserType;
import io.restassured.response.Response;
import pojoClasses.Api;
import pojoClasses.GetCourse;
import pojoClasses.WebAutomation;

import static io.restassured.RestAssured.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OAuthTest {


	@Test
	public void authTest() throws InterruptedException {

		//Open URL and get the code
		WebDriver driver;

		String[] AllCourses = {"Selenium Webdriver Java","Cypress","Protractor"};

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
		Response response = 
				given().queryParam("access_token", AccessToken).expect().defaultParser(Parser.JSON)
				.when().get("https://rahulshettyacademy.com/getCourse.php");

		//Asserting status code
		int statuscode = response.getStatusCode();
		Assert.assertEquals(statuscode, 200);


		//Converting Java object to json response body using Deserialization   
		GetCourse CourseList = response.as(GetCourse.class);


		System.out.println(CourseList.getInstructor());
		System.out.println(CourseList.getLinkedIn());
		System.out.println(CourseList.getCourses().getApi().get(1).getCourseTitle());

		//Getting price of selenium course
		List<Api> ApiCourse = CourseList.getCourses().getApi();
		for(int i=0; i<ApiCourse.size(); i++) {

			if(ApiCourse.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(ApiCourse.get(i).getPrice());
			}
		}

		//Asserting course titles
		List<WebAutomation> webautomation = CourseList.getCourses().getWebAutomation();
		ArrayList<String> List = new ArrayList<String>();

		for(int i=0; i<webautomation.size(); i++) {
			List.add(CourseList.getCourses().getWebAutomation().get(i).getCourseTitle()); 

		}

		List<String> expectedcourses= Arrays.asList(AllCourses);

		Assert.assertEquals(List, expectedcourses);
	}





}
