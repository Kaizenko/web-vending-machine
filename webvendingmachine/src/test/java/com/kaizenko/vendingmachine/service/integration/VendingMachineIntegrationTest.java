package com.kaizenko.vendingmachine.service.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.kaizenko.vendingmachine.dao.FakePaymentDao;
import com.kaizenko.vendingmachine.dao.PaymentDao;
import com.kaizenko.vendingmachine.domain.Product;
import com.kaizenko.vendingmachine.service.VendingMachine;

@SpringBootTest()
@ActiveProfiles("test")
@Transactional
class VendingMachineIntegrationTest {

	@Autowired
	private VendingMachine vendingMachine;
	
	@BeforeEach
	void beforeEach() {
		//interchange actual wiring with straight up injection of fakes
		//vendingMachine=new VendingMachine();
		//CoinPaymentService paymentService=new CoinPaymentService();
		//paymentService.setPaymentDao(new FakePaymentDao());
		//vendingMachine.setPaymentService(paymentService);
	}
	
	@Test
	void makeSelection_WhenNoPayment_Expect_NoProduct() {
		Product product=vendingMachine.makeSelection();
		assertThat(product).isNull();		
	}
	
	@Test
	void makeSelection_WhenNotEnoughPayment_Expect_NoProduct() {
		vendingMachine.makePayment();
		Product product=vendingMachine.makeSelection();
		assertThat(product).isNull();		
	}
	
	@Test
	void makeSelection_WhenPayment_Expect_Product() {
		vendingMachine.makePayment();
		vendingMachine.makePayment();
		Product product=vendingMachine.makeSelection();
		assertThat(product).isNotNull();		
	}
	
	@Test
	void releaseChange_WhenNoChange_Expect_0() {
		int change=vendingMachine.releaseChange();
		assertThat(change).isEqualTo(0);		
	}
	
	@Test
	void releaseChange_WhenChange_Expect_Change() {
		vendingMachine.makePayment();		
		int change=vendingMachine.releaseChange();
		assertThat(change).isEqualTo(25);		
	}
	
	@Test
	void releaseChange_WhenAfterExactPayment_Expect_NoChange() {
		vendingMachine.makePayment();	
		vendingMachine.makePayment();	
		vendingMachine.makeSelection();	
		int change=vendingMachine.releaseChange();
		assertThat(change).isEqualTo(0);		
	}
	
	@Test
	void releaseChange_WhenAfterExtraPayment_Expect_Change() {
		vendingMachine.makePayment();	
		vendingMachine.makePayment();
		vendingMachine.makePayment();
		vendingMachine.makeSelection();	
		int change=vendingMachine.releaseChange();
		assertThat(change).isEqualTo(25);		
	}
	
	@Test
	void releaseChange_WhenChangeAlreadyReleased_Expect_NoChange() {
		vendingMachine.makePayment();	
		vendingMachine.makePayment();
		vendingMachine.makePayment();
		vendingMachine.makeSelection();	
		vendingMachine.releaseChange();
		int change=vendingMachine.releaseChange();
		assertThat(change).isEqualTo(0);		
	}
	
	@Test
	void getMessage_WhenNoPayment_Expect_InsertCoinMessage() {
		vendingMachine.makeSelection();		
		String message=vendingMachine.getMessage();
		assertThat(message).isEqualTo("Please insert coins");	
	}
	
	@Test
	void getMessage_WhenSelectionWithPayment_Expect_EnjoyMessage() {
		vendingMachine.makePayment();	
		vendingMachine.makePayment();
		vendingMachine.makeSelection();			
		String message=vendingMachine.getMessage();
		assertThat(message).isEqualTo("Enjoy!");	
	}	
	
}
