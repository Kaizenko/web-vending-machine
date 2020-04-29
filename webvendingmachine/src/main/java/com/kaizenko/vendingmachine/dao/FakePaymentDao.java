package com.kaizenko.vendingmachine.dao;

import org.springframework.stereotype.Component;

@Component
public class FakePaymentDao implements PaymentDao {

	private int payment=0;
	
	public int retrieve() {		
		return payment;
	}

	public void update(int money) {
		payment=money;		
	}

}
