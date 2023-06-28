package restAssuredPractice;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import dataFiles.CreatePayload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class DynamicJason {
	
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String response = given().header("Content-Type","application/json").body(CreatePayload.addBook(isbn,aisle))
		.when().post("Library/Addbook.php")
		.then().assertThat().statusCode(200).body("Msg", equalTo("successfully added")).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	
	@DataProvider(name = "BooksData")
	public Object[][] dataSet() {
		
		return new Object[][] {{"TESMN","7874"}, {"HGYR","8907"}, {"HSGBE","90988"}};
		
	}

}
