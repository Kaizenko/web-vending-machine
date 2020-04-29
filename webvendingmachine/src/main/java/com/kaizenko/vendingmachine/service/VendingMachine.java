package com.kaizenko.vendingmachine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaizenko.vendingmachine.domain.Product;

@Component
public class VendingMachine {

	@Autowired
	PaymentService paymentService;
	private String message="Please insert coins";
	private final int PRODUCT_PRICE=50;
	private final int QUARTER=25;

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public Product makeSelection() {
		if (paymentService.isPaymentMade(PRODUCT_PRICE)) {
			paymentService.processPayment(PRODUCT_PRICE);
			message="Enjoy!";
			return new Product();
		}
		else {
			message="Please insert coins";
			return null;
		}
	}

	public void makePayment() {			
		paymentService.makePayment(QUARTER);	
		if (paymentService.isPaymentMade(PRODUCT_PRICE)) {
			message="You can now make a purchase";			
		}
		else {
			message="Please insert coins";			
		}
	}

	public int releaseChange() {
		int change=paymentService.getPayment();		
		paymentService.processPayment(change);
		if (change>0) {
			message="Remember to take your change";
		}
		else {
			message="Please insert coins";	
		}
		return change;
	}

	public int getPayment() {		
		return paymentService.getPayment();
	}

	public String getMessage() {
		return message;
	}

}
