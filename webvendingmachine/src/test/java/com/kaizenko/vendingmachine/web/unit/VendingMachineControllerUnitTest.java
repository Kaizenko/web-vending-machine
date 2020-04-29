package com.kaizenko.vendingmachine.web.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.kaizenko.vendingmachine.service.VendingMachine;
import com.kaizenko.vendingmachine.web.VendingMachineController;

@ExtendWith(MockitoExtension.class)
class VendingMachineControllerUnitTest {
	
	@Mock
	VendingMachine mockVendingMachine;
	
	@InjectMocks
	private VendingMachineController vendingMachineController;

	@Test
	void vendingMachineForm_WhenCalled_ExpectKeysInModel() {
		Model model=new ExtendedModelMap();
		vendingMachineController.vendingMachineForm(model);
		assertThat(model.asMap()).containsKeys("payment","message","change");		
	}
	
	@Test
	void vendingMachineForm_WhenCalled_ExpectFilledModel() {
		Model model=new ExtendedModelMap();
		when(mockVendingMachine.getMessage()).thenReturn("msg");
		when(mockVendingMachine.getPayment()).thenReturn(1);
		vendingMachineController.vendingMachineForm(model);
		assertThat(model.asMap()).contains(
				entry("payment", 1),
				entry("message", "msg"),
				entry("change", 0));		
	}
	
	@Test
	void makePayment_WhenCalled_Expect_redirectToVendingMaching() {
		Model model=new ExtendedModelMap();				
		String view=vendingMachineController.makePayment("action", model);
		assertThat(view).isEqualTo("redirect:/vendingmachine");				
	}
	
	@Test
	void makePayment_WhenMakePaymentAction_ExpectCallToMakePayment() {
		Model model=new ExtendedModelMap();				
		String view=vendingMachineController.makePayment("makePayment", model);
		verify(mockVendingMachine).makePayment();		
	}
	
	@Test
	void makePayment_WhenBuyProductAction_ExpectCallToMakeSelection() {
		Model model=new ExtendedModelMap();				
		String view=vendingMachineController.makePayment("buyProduct", model);
		verify(mockVendingMachine).makeSelection();		
	}
	
	@Test
	void makePayment_WhenReleaseChangeAction_ExpectCallToReleaseChange() {
		Model model=new ExtendedModelMap();				
		String view=vendingMachineController.makePayment("releaseChange", model);
		verify(mockVendingMachine).releaseChange();		
	}	
	 
}
