package com.kaizenko.vendingmachine.dao;

public interface PaymentDao {
	public int retrieve();
	public void update(int money);	
}