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
import com.kaizenko.vendingmachine.service.CoinPaymentService;

@SpringBootTest()
@ActiveProfiles("test")
@Transactional
@Rollback
class CoinPaymentServiceIntegrationTest {

	@Autowired
	private CoinPaymentService paymentService;
	
	@BeforeEach
	void beforeEach() {
		//interchange actual wiring with straight up injection of fakes
		//paymentService=new CoinPaymentService();
		//paymentService.setPaymentDao(new FakePaymentDao());
	}
	
	@Test
	void isPaymentMade_When_NotMade_Expect_False() {
		boolean isPaymentMade=paymentService.isPaymentMade(25);
		assertThat(isPaymentMade).isFalse();
	}
	
	@Test
	void isPaymentMade_When_Made_Expect_True() {
		paymentService.makePayment(50);
		boolean isPaymentMade=paymentService.isPaymentMade(25);
		assertThat(isPaymentMade).isTrue();
	}
	
	@Test
	void processPayment_When_PaymentIs100AndPriceIs25_Expect_PaymentToBe75() {
		paymentService.makePayment(100);
		paymentService.processPayment(25);
		assertThat(paymentService.getPayment()).isEqualTo(75);	
	}	
	
	@Test
	void makePayment_When_PaymentIs25_Expect_PaymentToBe25() {
		paymentService.makePayment(25);		
		assertThat(paymentService.getPayment()).isEqualTo(25);	
	}	
	
	@Test
	void makePayment_When_2PaymentsOf25_Expect_PaymentToBe50() {
		paymentService.makePayment(25);	
		paymentService.makePayment(25);	
		assertThat(paymentService.getPayment()).isEqualTo(50);	
	}	

}
