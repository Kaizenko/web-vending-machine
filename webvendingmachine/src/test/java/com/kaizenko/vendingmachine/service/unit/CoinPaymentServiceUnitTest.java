package com.kaizenko.vendingmachine.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kaizenko.vendingmachine.dao.PaymentDao;
import com.kaizenko.vendingmachine.service.CoinPaymentService;

@ExtendWith(MockitoExtension.class)
class CoinPaymentServiceUnitTest {

	@Mock
	private PaymentDao paymentDao;
	
	@InjectMocks
	private CoinPaymentService paymentService;
	
	
	
	@Test
	void isPaymentMade_When_NotMade_Expect_False() {		
		boolean isPaymentMade=paymentService.isPaymentMade(25);
		assertThat(isPaymentMade).isFalse();
	}
	
	@Test
	void isPaymentMade_When_PaymentMade_Expect_True() {
		when(paymentDao.retrieve()).thenReturn(25);
		boolean isPaymentMade=paymentService.isPaymentMade(25);
		assertThat(isPaymentMade).isTrue();
	}
	
	@Test
	void processPayment_When_PaymentIs100AndPriceIs25_Expect_UpdateWith75() {
		when(paymentDao.retrieve()).thenReturn(100);
		paymentService.processPayment(25);
		verify(paymentDao).update(75);		
	}
	
	@Test
	void processPayment_When_NoInitialPayment_Expect_UpdateWith0() {
		when(paymentDao.retrieve()).thenReturn(0);
		paymentService.processPayment(25);
		verify(paymentDao).update(0);		
	}
	
	@Test
	void makePayment_When_Payment25_Expect_UpdateWith25() {
		when(paymentDao.retrieve()).thenReturn(0);
		paymentService.makePayment(25);
		verify(paymentDao).update(25);		
	}
	
	@Test
	void makePayment_When_Payment25WithInitialPayment50_Expect_UpdateWith75() {
		when(paymentDao.retrieve()).thenReturn(50);
		paymentService.makePayment(25);
		verify(paymentDao).update(75);		
	}
	
	

}
