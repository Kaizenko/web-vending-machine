package com.kaizenko.vendingmachine.web.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.kaizenko.vendingmachine.service.VendingMachine;


@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
class VendingMachineControllerWithMockMVCIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;	

	
	@Test
	void vendingMachine_When_Get_Expect_VendingMachineView() throws Exception {
		ModelAndView mv=mockMvc.perform(get("/vendingmachine"))
						.andExpect(status().isOk())
						.andReturn().getModelAndView(); 								
		String viewName=mv.getViewName();
		assertThat(viewName).isEqualTo("vendingmachine");
	}
	
	@Test
	void vendingMachine_When_Post_Expect_RedirectToVendingMachineView() throws Exception {
		ModelAndView mv=mockMvc.perform(post("/vendingmachine")
							.param("actionType", "makePayment"))
							.andExpect(status().is3xxRedirection())
							.andReturn().getModelAndView(); 								
		String viewName=mv.getViewName();
		assertThat(viewName).isEqualTo("redirect:/vendingmachine");
	}
		
	@Test
	void vendingMachine_When_PostMakePayment_Expect_PaymentInModel() throws Exception {
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		//a manual extra get call is needed for testing because mockMvc does not handle redirects
		ModelAndView mv=mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView(); 	
		assertThat(mv.getModel()).contains(entry("payment",25));
	}
	
	@Test
	void vendingMachine_When_PostReleaseChangeAfterMakingPayment_Expect_ChangeInModel() throws Exception {
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "releaseChange"));
		//a manual extra get call is needed for testing because mockMvc does not handle redirects
		ModelAndView mv=mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView(); 
		assertThat(mv.getModel()).contains(entry("change",25),
											entry("payment",0),
											entry("message","Remember to take your change"));
	}		
	
	@Test	
	void vendingMachine_When_PostReleaseChangeWithNoPayment_Expect_NoChangeInModel() throws Exception {
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "releaseChange"));
		//a manual extra get call is needed for testing because mockMvc does not handle redirects
		ModelAndView mv=mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView(); 
		assertThat(mv.getModel()).contains(entry("change",0),
											entry("payment",0));
	}		
	
	@Test
	void vendingMachine_When_PostBuyProductWithNoPayment_Expect_MsgInModel() throws Exception {
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "buyProduct"));
		//a manual extra get call is needed for testing because mockMvc does not handle redirects
		ModelAndView mv=mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView(); 
		assertThat(mv.getModel()).contains(entry("payment",25),
											entry("message","Please insert coins"));
	}		
	
	@Test
	void vendingMachine_When_PostBuyProductWithPayment_Expect_EnjoyMsgInModel() throws Exception {
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "buyProduct"));
		//a manual extra get call is needed for testing because mockMvc does not handle redirects
		ModelAndView mv=mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView(); 
		assertThat(mv.getModel()).contains(entry("payment",0),
											entry("change",0),		
											entry("message","Enjoy!"));
	}		
	
	@Test
	void vendingMachine_When_PostBuyProductWithExtraPayment_Expect_PaymentInModel() throws Exception {
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));
		mockMvc.perform(post("/vendingmachine")
				.param("actionType", "buyProduct"));
		//a manual extra get call is needed for testing because mockMvc does not handle redirects
		ModelAndView mv=mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView(); 
		assertThat(mv.getModel()).contains(entry("payment",25));											
											
	}		

	
}
