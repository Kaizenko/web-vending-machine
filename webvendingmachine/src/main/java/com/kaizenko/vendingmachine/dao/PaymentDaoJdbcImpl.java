package com.kaizenko.vendingmachine.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class PaymentDaoJdbcImpl implements PaymentDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int retrieve() {
		int payment=jdbcTemplate.queryForObject("SELECT amount FROM payment where id=?", new Object[] {1}, Integer.class);
		return payment;
	}

	@Override
	public void update(int payment) {
		jdbcTemplate.update("UPDATE payment set amount = ? where id=?", payment, 1);
	}	
	
	void setJdbcTemaplte(JdbcTemplate jdbcTemplate) {
	   	this.jdbcTemplate=jdbcTemplate;
	} 	
		
}
