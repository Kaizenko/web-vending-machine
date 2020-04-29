package com.kaizenko.vendingmachine.service;

public interface PaymentService {

	boolean isPaymentMade(int price);

	void processPayment(int price);
	
	void makePayment(int paymentAmount);

	int getPayment();	

}