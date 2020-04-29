package com.kaizenko.vendingmachine.web.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.kaizenko.vendingmachine.service.VendingMachine;
import com.kaizenko.vendingmachine.testcategory.BrowserTests;


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
	static void beforeAll() throws MalformedURLException {		
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
