package com.kaizenko.vendingmachine.dao.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.kaizenko.vendingmachine.dao.PaymentDaoJdbcImpl;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback(true) //For demo purposes because True is the default setting
class PaymentDaoJdbcIntegrationTest {	
 
    @Autowired
    private PaymentDaoJdbcImpl paymentDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;	
    
    @Test
	void update_When_Called_Expect_SameRowCount() {    	
    	int rowCountBeforeUpdate=JdbcTestUtils.countRowsInTable(jdbcTemplate,"payment");
    	
    	paymentDao.update(10);    	
    	
    	int updatedPayment=jdbcTemplate.queryForObject("select amount from payment where id=1", new Integer[] {},Integer.class);
    	int rowCountAfterUpdate=JdbcTestUtils.countRowsInTable(jdbcTemplate,"payment");
    	assertEquals(rowCountBeforeUpdate, rowCountAfterUpdate);      	 
	}	
      
    @Test    
	void update_When_50_Expect_50() {    	
    	int payment=50;
    	
    	paymentDao.update(payment);    	
    	
    	int updatedPayment=jdbcTemplate.queryForObject("select amount from payment where id=1", new Integer[] {},Integer.class);
    	assertThat(updatedPayment).isEqualTo(payment);   
	}	
    
    @Test
	void retrieve_When_25_Expect_25() {
    	jdbcTemplate.update("update payment set amount=25 where id=1");
    	int payment=paymentDao.retrieve();
    	assertThat(payment).isEqualTo(25);
	}	
    
    @Test
	void retrieve_When_0_Expect_0() {
    	int payment=paymentDao.retrieve();
    	assertThat(payment).isEqualTo(0);    	 
	}	
    //To Demo transaction rollbacks, uncomment and set roll back to false
    /*
    @Test
    void dummyTestToMessWithTheDatabase() {
    	paymentDao.update(1);   	 
	}	
    @Test
	void retrieveDemo_When_TransactionNotRolledBack_Expect_TestToFail() {
    	int payment=paymentDao.retrieve();
    	assertThat(payment).isEqualTo(0);    	 
	}
		*/
}
