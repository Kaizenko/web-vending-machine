package com.kaizenko.vendingmachine.web.acceptance;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
	
	int initialPayment; //initial payment can be used if we are not reseting payments
	WebDriver driver;
	URL local;	
	
	@Before	
	public void rollBackBeforeHook() throws MalformedURLException {
		jdbcTemplate.update("update payment set amount=0 where id=1");
		local = new URL("http://localhost:"+port);	
		driver = new ChromeDriver();			
	}

	@After
	public void rollBackAfterHook() {
		driver.close();
	}
	
	  
	public VendingMachineControllerStepDef() {			
		When("at the {string}", (String url) -> {		   
			driver.get(local+"/"+url);
			//use initialAmount is not always reseting in @before
			initialPayment=getPayment();
		});

		And("I make a payment", () -> {
			driver.findElement(By.id("makePayment")).click();				
		});		
		
		And("I release change", () -> {
			driver.findElement(By.id("releaseChange")).click();					
		});		

		Then("the amount paid is the initial amount plus {int}", (Integer payment) -> {			
			assertThat(getPayment()).isEqualTo(initialPayment+payment);			
		});	
		
		Then("the amount paid is {int}", (Integer payment) -> {				
			assertThat(getPayment()).isEqualTo(payment);			
		});	
		
		Then("the change released is {int}", (Integer change) -> {			
			assertThat(getChange()).isEqualTo(change);			
		});	
		
	}	
	
	private int getPayment() {		
		return getWebElementIntValue("payment");
	}
	
	private int getChange() {
		return getWebElementIntValue("change");
	}
	
	private int getWebElementIntValue(String element) {
		WebElement paymentWebElement=driver.findElement(By.id(element));
		Integer value=Integer.valueOf(paymentWebElement.getText());
		return value;
	}
	
}

