package restAssuredPractice;

import org.testng.annotations.Test;

import e2ePojoRequest.Login;
import e2ePojoRequest.PlaceOrderDetailPojo;
import e2ePojoRequest.PlaceOrderPojo;
import e2ePojoResponse.LoginResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EcommerceWebTest {

	@Test
	public void E2ETest() {

		//Building Spec Builder
		RequestSpecification uri = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();

		//Login Object Creation 
		Login login = new Login();
		login.setUserEmail("vprabhugaonkar13@gmail.com");
		login.setUserPassword("Restinpeace@123");

		//Login Post Call
		Response generateAccessToken = given().spec(uri).body(login)
				.when().post("api/ecom/auth/login");
		LoginResponse tokenrequest = generateAccessToken.as(LoginResponse.class);
		String token = tokenrequest.getToken();
		String userID = tokenrequest.getUserId();

		//Create a Product
		RequestSpecification baseSpecCreateProduct = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();
		String CreateProduct = given().spec(baseSpecCreateProduct)
				.param("productName", "Tshirt")
				.param("productAddedBy", userID)
				.param("productCategory", "fashion")
				.param("productSubCategory", "Tshirt")
				.param("productPrice", "1234")
				.param("productDescription", "Test Product")
				.param("productFor", "Women")
				.multiPart("productImage", new File("/Users/vaibhav/Downloads/Shirt.png"))
				.when().post("api/ecom/product/add-product")
				.then().assertThat().statusCode(201).body("message", equalTo("Product Added Successfully")).extract().response().asString();

		JsonPath js1 = new JsonPath(CreateProduct);
		String productID = js1.getString("productId");


		//Place order
		RequestSpecification baseSpecPlaceOrder = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).setContentType(ContentType.JSON).build();

		PlaceOrderDetailPojo placeorderdetails = new PlaceOrderDetailPojo();
		placeorderdetails.setCountry("India");
		placeorderdetails.setProductOrderedId(productID);

		List<PlaceOrderDetailPojo> orderdetaillist = new ArrayList<PlaceOrderDetailPojo>();
		orderdetaillist.add(placeorderdetails);

		PlaceOrderPojo placeorder = new PlaceOrderPojo();
		placeorder.setOrders(orderdetaillist);

		String placeingorder = given().spec(baseSpecPlaceOrder).body(placeorder)
				.when().post("api/ecom/order/create-order")
				.then().extract().response().asString();

		System.out.println(placeingorder);

		JsonPath path = new JsonPath(placeingorder);
		String OrderValue = path.getString("orders");
		String OrderID = OrderValue.substring(1, OrderValue.length() - 1);
		System.out.println(OrderID);



		//Delete product
		RequestSpecification baseDelete = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();

		String DeleteCall = given().spec(baseDelete).pathParam("productId", productID)
				.when().delete("api/ecom/product/delete-product/{productId}")
				.then().assertThat().statusCode(200).body("message", equalTo("Product Deleted Successfully")).extract().response().asString();
		System.out.println(DeleteCall);

		//Delete Order
		String OrderDelete = given().spec(baseDelete).pathParam("OrderID", OrderID)
				.when().delete("api/ecom/order/delete-order/{OrderID}")
				.then().assertThat().statusCode(200).body("message", equalTo("Orders Deleted Successfully")).extract().response().asString();
		System.out.println(OrderDelete);



















	}


}
