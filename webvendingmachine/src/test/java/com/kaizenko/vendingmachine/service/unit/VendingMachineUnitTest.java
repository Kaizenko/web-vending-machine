package com.kaizenko.vendingmachine.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kaizenko.vendingmachine.domain.Product;
import com.kaizenko.vendingmachine.service.PaymentService;
import com.kaizenko.vendingmachine.service.VendingMachine;

@ExtendWith(MockitoExtension.class)
class VendingMachineUnitTest {
	
	@Mock 
	PaymentService mockPaymentService;
	
	@InjectMocks
	private VendingMachine vendingMachine;

	@Test
	void makeSelection_WhenNotPaymentMade_ExpectNoProduct() {
		when(mockPaymentService.isPaymentMade(50)).thenReturn(false);
		Product product=vendingMachine.makeSelection();
		assertThat(product).isNull();
	}
	
	@Test
	void makeSelection_WhenPaymentMade_ExpectProduct() {
		when(mockPaymentService.isPaymentMade(50)).thenReturn(true);
		Product product=vendingMachine.makeSelection();		
		assertThat(product).isNotNull();
	}
	
	@Test
	void makePayment_WhenCalled_ExpectCallToMakePaymentWith25() {
		vendingMachine.makePayment(); 
		verify(mockPaymentService).makePayment(25);		
	}
	
	@Test
	void releaseChange_WhenNoChange_Expect0() {
		int change=vendingMachine.releaseChange(); 
		assertThat(change).isEqualTo(0);
	}
	
	@Test
	void releaseChange_WhenChange25_Expect25() {
		when(mockPaymentService.getPayment()).thenReturn(25);
		int change=vendingMachine.releaseChange(); 
		assertThat(change).isEqualTo(25);
	}
	
	@Test
	void releaseChange_WhenChange25_ExpectCallToProcessPaymentWith25() {
		when(mockPaymentService.getPayment()).thenReturn(25);
		int change=vendingMachine.releaseChange(); 
		verify(mockPaymentService).processPayment(25);	
	}
	
	@Test
	void getMessage_WhenDefaullt_ExpectInsertCoinMessage() {
		String message=vendingMachine.getMessage(); 
		assertThat(message).isEqualTo("Please insert coins");
	}
	
	@Test
	void getMessage_WhenSelectionWithoutPayment_ExpectInsertCoinMessage() {
		when(mockPaymentService.isPaymentMade(50)).thenReturn(false);
		vendingMachine.makeSelection();
		String message=vendingMachine.getMessage(); 
		assertThat(message).isEqualTo("Please insert coins");
	}
	
	@Test
	void getMessage_WhenSelectionWithPayment_ExpectEnjoyMessage() {
		when(mockPaymentService.isPaymentMade(50)).thenReturn(true);
		vendingMachine.makeSelection();
		String message=vendingMachine.getMessage(); 
		assertThat(message).isEqualTo("Enjoy!");
	}	
	

}
