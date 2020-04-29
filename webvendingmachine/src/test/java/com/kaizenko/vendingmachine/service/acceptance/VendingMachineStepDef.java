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
		
	int initialAmount=0;
	int change;
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
		
		When("at the {string}", (String url) -> {		   
			initialAmount=vendingMachine.getPayment();
		});

		And("I make a payment", () -> {
			vendingMachine.makePayment();		
		});		
		
		And("I release change", () -> {
			change=vendingMachine.releaseChange();
		});		

		Then("the amount paid is the initial amount plus {int}", (Integer payment) -> {			
			Integer updatePayment=vendingMachine.getPayment();
			assertThat(updatePayment).isEqualTo(initialAmount+payment);		
		});	
		
		Then("the amount paid is {int}", (Integer payment) -> {			
			Integer updatePayment=vendingMachine.getPayment();
			assertThat(updatePayment).isEqualTo(payment);			
		});
		
		Then("the change released is {int}", (Integer change) -> {
			assertThat(change).isEqualTo(change);	
		});
	}	
	
}

