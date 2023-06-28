package restAssuredPractice;

import org.testng.Assert;
import org.testng.annotations.Test;

import dataFiles.CreatePayload;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.*;

public class FirstTest {
	
	
	
	@Test
	public void PostRequest() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//Post Request
		String CreateResponse = given().queryParam("key","qaclick123").header("Content-Type","application/json").body(CreatePayload.addPlacePayload())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		JsonPath js = new JsonPath(CreateResponse);
		String PlaceID = js.getString("place_id");
		
		String UpdateAddress = "71 winter walk, USA";
		//Put Request
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body("{\n"
				+ "\"place_id\":\""+PlaceID+"\",\n"
				+ "\"address\":\""+UpdateAddress+"\",\n"
				+ "\"key\":\"qaclick123\"\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Request
		String GetRespose = given().queryParam("key", "qaclick123").queryParam("place_id", PlaceID)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		System.out.println(GetRespose);
		
		JsonPath js1 = new JsonPath(GetRespose);
		String address = js1.getString("address");
		Assert.assertEquals(address, UpdateAddress);
		
		
	}

}
