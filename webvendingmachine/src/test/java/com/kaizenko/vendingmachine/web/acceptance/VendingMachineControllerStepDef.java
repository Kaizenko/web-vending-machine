package com.kaizenko.vendingmachine.web.acceptance;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java8.En; 

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("BrowserTests")
public class VendingMachineControllerStepDef implements En {

	@LocalServerPort
    private int port;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	
	WebDriver driver;
	URL local;	
	
	@Before	
	public void beforeHook() throws MalformedURLException {
		jdbcTemplate.update("update payment set amount=0 where id=1");
		local = new URL("http://localhost:"+port);	
		driver = new ChromeDriver();			
	}

	@After
	public void afterHook() {
		driver.close();
	}
	
	public VendingMachineControllerStepDef() {			

		Given("I'm at a vending machine", () -> {
		   driver.get(local+"/vendingmachine");
	    });
		
		Given("a vending machine with {int} cents", (Integer cents) -> {
		    int numberOfQuarters=cents/25;
		    for (int i=0;i<numberOfQuarters;i++) {
		    	driver.findElement(By.id("makePayment")).click();
		    }
		});
		
		When("I buy a product", () -> {
			driver.findElement(By.id("buyProduct")).click();
		});
		
		Then("the vending machine should not dispense a product", () -> {
			assertThat(getProduct()).isBlank();
		});
		
		Then("the vending machine should dispense a product", () -> {
			assertThat(getProduct()).isNotBlank();
		});
		
		When("I insert a coin", () -> {
			driver.findElement(By.id("makePayment")).click();
		});
		
		Then("the vending machine should have a total of {int} cents", (Integer expectedPayment) -> {
		   assertThat(getPayment()).isEqualTo(expectedPayment);
		});
		
		Then("the vending machine should release {int} cents", (Integer expectedChange) -> {
			 assertThat(getChange()).isEqualTo(expectedChange); 
		});
		
		When("I release change", () -> {
			driver.findElement(By.id("releaseChange")).click();					
		});		
		
	}	
	
	private int getPayment() {		
		return getWebElementIntValue("payment");
	}
	
	private int getChange() {
		return getWebElementIntValue("change");
	}
	
	private String getProduct() {
		WebElement productWebElement=driver.findElement(By.id("product"));
		return productWebElement.getText();
	}
	
	private int getWebElementIntValue(String element) {
		WebElement paymentWebElement=driver.findElement(By.id(element));
		Integer value=Integer.valueOf(paymentWebElement.getText());
		return value;
	}
	
}

