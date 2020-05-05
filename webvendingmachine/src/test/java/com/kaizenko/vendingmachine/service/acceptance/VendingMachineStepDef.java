package com.kaizenko.vendingmachine.service.acceptance;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import javax.transaction.Transactional;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.kaizenko.vendingmachine.domain.Product;
import com.kaizenko.vendingmachine.service.VendingMachine;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import io.cucumber.java8.En;
import io.cucumber.java.Before;
import io.cucumber.java.After;

 
@SpringBootTest()
@ActiveProfiles("test")
public class VendingMachineStepDef implements En {
		
	int change;
	Product product;
	@Autowired
	VendingMachine vendingMachine;
	
	private TransactionStatus txStatus;

	@Autowired
	private PlatformTransactionManager txMgr;

	@Before	
	public void rollBackBeforeHook() {
	    txStatus = txMgr.getTransaction(new DefaultTransactionDefinition());
	}

	@After
	public void rollBackAfterHook() {
	    txMgr.rollback(txStatus);
	}
	
	public VendingMachineStepDef() {
		Given("I'm at a vending machine", () -> {
		    //nothing to do since vending machine is autowired
		});

		Given("a vending machine with {int} cents", (Integer cents) -> {
		    int numberOfQuarters=cents/25;
		    for (int i=0;i<numberOfQuarters;i++) {
		    	vendingMachine.makePayment();
		    }
		});

		When("I buy a product", () -> {
		    product=vendingMachine.makeSelection();
		});

		Then("the vending machine should not dispense a product", () -> {
		    assertThat(product).isNull();
		});

		Then("the vending machine should dispense a product", () -> {
		   assertThat(product).isNotNull();
		});

		When("I insert a coin", () -> {
		    vendingMachine.makePayment();
		});

		Then("the vending machine should have a total of {int} cents", (Integer expectedPayment) -> {
		    assertThat(vendingMachine.getPayment()).isEqualTo(expectedPayment);
		});
		
		When("I release change", () -> {
			change=vendingMachine.releaseChange();
		});

		Then("the vending machine should release {int} cents", (Integer expectedChange) -> {
			assertThat(change).isEqualTo(expectedChange);
		});

		
	}	
	
}

