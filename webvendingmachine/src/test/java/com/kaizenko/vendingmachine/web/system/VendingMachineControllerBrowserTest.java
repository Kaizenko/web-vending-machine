package com.kaizenko.vendingmachine.web.system;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
import com.kaizenko.vendingmachine.testcategory.BrowserTests;

import io.cucumber.java.After;
import io.cucumber.java.Before;

@Category(BrowserTests.class)
@Tag("BrowserTests")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class VendingMachineControllerBrowserTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static WebDriver driver;
	private URL baseUrl;
		
	@LocalServerPort
	int port;
		
	@BeforeAll
	static void beforeAll() {
		driver = new ChromeDriver();
	}
	
	@BeforeEach
	void beforeEach() throws MalformedURLException {		
		baseUrl= new URL("http://localhost:"+port);
		jdbcTemplate.update("update payment set amount=0 where id=1");
	}
	
	@AfterAll
	static void afterAll() {
		driver.close();
	}

	@Test
	void defaultCall_Expect_PaymentToBe0() {
		//arrange
		
		//act
		driver.get(baseUrl+"/vendingmachine");		
		
		//assert
		WebElement payment =driver.findElement(By.id("payment"));
		assertThat(payment.getText()).isEqualTo(String.valueOf(0));		
	}
	
	@Test
	void makePayment_WhenClickedTwice_Expect_Payment50() {
		//arrange
		driver.get(baseUrl+"/vendingmachine");	
		
		//act
		driver.findElement(By.id("makePayment")).click();
		driver.findElement(By.id("makePayment")).click();
		
		//assert
		WebElement paymentElement =driver.findElement(By.id("payment"));
		Integer afterPayment=Integer.valueOf(paymentElement.getText());
		assertThat(afterPayment).isEqualTo(50);
	}
	
	@Test
	void makePayment_WhenClicked_Expect_Payment25() {
		//arrange
		driver.get(baseUrl+"/vendingmachine");	
		WebElement paymentElement =driver.findElement(By.id("payment"));
		
		//act
		driver.findElement(By.id("makePayment")).click();
		
		//assert
		paymentElement =driver.findElement(By.id("payment"));
		Integer afterPayment=Integer.valueOf(paymentElement.getText());
		assertThat(afterPayment).isEqualTo(25);
	}
	
	@Test	
	void releaseChange_WhenClicked_Expect_Payment0() {
		//arrange		
		driver.get(baseUrl+"/vendingmachine");	
		driver.findElement(By.id("makePayment")).click();
		
		//act
		driver.findElement(By.id("releaseChange")).click();
		
		//assert
		WebElement paymentElement =driver.findElement(By.id("payment"));
		Integer payment=Integer.valueOf(paymentElement.getText());
		assertThat(payment).isEqualTo(0);
	}
	
	@Test	
	void releaseChange_WhenClickedAfterPayment_Expect_Change() {
		//arrange		
		driver.get(baseUrl+"/vendingmachine");		
		driver.findElement(By.id("makePayment")).click();
		driver.findElement(By.id("makePayment")).click();
		
		//act
		driver.findElement(By.id("releaseChange")).click();
		
		//assert
		WebElement paymentElement =driver.findElement(By.id("change"));
		Integer change=Integer.valueOf(paymentElement.getText());
		assertThat(change).isEqualTo(50);
	}	
}
