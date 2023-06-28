package restAssuredPractice;

import org.testng.annotations.Test;

import dataFiles.CreatePayload;
import io.restassured.path.json.JsonPath;

public class ComplexJason {
	
	@Test
	public static void complexJasonTest() {
		int sum = 0;
		
	JsonPath js = new JsonPath(CreatePayload.coursePrice());
	
	//Print the count of the courses
	int count = js.getInt("courses.size()");
	System.out.println(count);
	
	//Print Purchase amount
	int PurchaseAmount = js.getInt("dashboard.purchaseAmount");
	System.out.println(PurchaseAmount);
	
	//Print the title of the first course
	String FirstCourseTitle = js.getString("courses[0].title");
	System.out.println(FirstCourseTitle);
	
	//Print all courses title and their respective price
	for(int i=0; i<count; i++) {
		String AllCoursesTitle = js.getString("courses["+i+"].title");
		System.out.println(AllCoursesTitle);
		int AllCoursesPrice =  js.getInt("courses["+i+"].price");
		System.out.println(AllCoursesPrice);
		
	}
	
	//Print no of copies sold by the RPA course
    for(int i=0; i<count; i++) {
    	String CourseTitle = js.getString("courses["+i+"].title");
    	if(CourseTitle.equals("RPA")) {
    		int Copies = js.getInt("courses["+i+"].copies");
    		System.out.println("Total no of copies are - "+Copies);
            
    	}
    	
    }
	
	
	//verify if sum of all courses matches the purchase amount
	for(int i=0; i<count; i++) {
		int price = js.getInt("courses["+i+"].price");
		int copiesValue = js.getInt("courses["+i+"].copies");
		int TotalValue = price*copiesValue;
		sum = sum+TotalValue;
	}
	System.out.println(sum);
	int purchaseAmount = js.getInt("dashboard.purchaseAmount");
	if(purchaseAmount == sum) {
		System.out.println("Sum of all courses and Total purchase amount matches");
	}
	else {
		System.out.println("Sum of all courses and Total purchase amount does not match");
	}
		
		
	}
	
	
	

}
