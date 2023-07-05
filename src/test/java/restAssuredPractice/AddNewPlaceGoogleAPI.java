package restAssuredPractice;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoClasses.AddPlaceGoogleAPI;
import pojoClasses.SubLocation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;


public class AddNewPlaceGoogleAPI {
	
	@Test
	public void addplace() {
		
		AddPlaceGoogleAPI place = new AddPlaceGoogleAPI();
		
		SubLocation l = new SubLocation();
		l.setLat(-58.383494);
		l.setLng(53.427362);
		
		place.setLocation(l);
		
		place.setAccuracy(50);
		place.setName("Frontline Test House");
		place.setPhone_number("(+91) 983 893 3937");
		place.setAddress("44, side layout, cohen 19");
		
		List<String> type = new ArrayList<String>();
		type.add("shoe park");
		type.add("shop");
		
		place.setTypes(type);
		place.setWebsite("www.test.com");
		place.setLanguage("English");
		
		
		//Request Spec Builder
		RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
	    ResponseSpecification res = new ResponseSpecBuilder().expectStatusCode(200).build();
	
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().spec(req)
		.body(place)
		.when().post("maps/api/place/add/json")
		.then().assertThat().spec(res).extract().response().asString();
		
		System.out.println("response is - "+response);
		
		
	}

}
