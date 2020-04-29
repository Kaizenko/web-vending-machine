package com.kaizenko.vendingmachine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kaizenko.vendingmachine.dao.PaymentDao;

@Component
public class CoinPaymentService implements PaymentService {
	
	@Autowired
	@Qualifier("jdbc")
	private PaymentDao paymentDao;

	@Override
	public boolean isPaymentMade(int price) {
		int payment=paymentDao.retrieve();
		if (payment>=price) {
			return true;
		}
		return false;
	}

	@Override
	public void processPayment(int price) {
		int payment=paymentDao.retrieve();
		if (payment>0) {
			payment=payment-price;
		}
		paymentDao.update(payment);		
	}

	@Override
	public int getPayment() {		
		return paymentDao.retrieve();
	}	

	@Override
	public void makePayment(int money) {
		int payment=paymentDao.retrieve();
		payment=payment+money;
		paymentDao.update(payment);			
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}
}
