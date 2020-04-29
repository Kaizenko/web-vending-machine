package com.kaizenko.vendingmachine.web.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import com.kaizenko.vendingmachine.service.VendingMachine;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VendingMachineControllerWithMockMVCUnitTest {
	//This is technically and integration test
	@Autowired
    private MockMvc mockMvc;	

	 @MockBean
	 private VendingMachine vendingMachine;

	@Test
	void vendingMachine_When_Get_Expect_VendingMachineView() throws Exception {
		ModelAndView mv=this.mockMvc.perform(get("/vendingmachine"))
									.andExpect(status().is2xxSuccessful())
									.andReturn()
									.getModelAndView();
		String viewName=mv.getViewName();
		assertThat(viewName).isEqualTo("vendingmachine");
	}	
	
	@Test
	void vendingMachine_WhenPost_Expect_RedirectToVendingMachineView() throws Exception {
		ModelAndView mv=this.mockMvc.perform(post("/vendingmachine")
								.param("actionType", "makePayment"))
								//.andDo(print())
								.andExpect(status().is3xxRedirection())
								.andReturn().getModelAndView(); 		
		assertThat(mv.getViewName()).isEqualTo("redirect:/vendingmachine");		
	}
	
	@Test
	void vendingMachine_When_Get_Expect_PaymentInModel() throws Exception {
		when(vendingMachine.getPayment()).thenReturn(25);
		ModelAndView mv=this.mockMvc.perform(get("/vendingmachine")).andReturn().getModelAndView();
		int payment=(int) mv.getModel().get("payment");
		assertThat(payment).isEqualTo(payment);
	}
	

	
	@Test
	void vendingMachine_WhenPostWithMakePayment_MakesCallToMakePayment() throws Exception {
		when(vendingMachine.getPayment()).thenReturn(25);
		this.mockMvc.perform(post("/vendingmachine")
				.param("actionType", "makePayment"));				
		verify(vendingMachine).makePayment();			
	}
	
	@Test
	void vendingMachine_WhenPostWithReleaseChange_MakesCallToReleaseChange() throws Exception {		
		this.mockMvc.perform(post("/vendingmachine")
				.param("actionType", "releaseChange"));				
		verify(vendingMachine).releaseChange();		
	}
	
	@Test
	void vendingMachine_WhenPostWithBuyProduct_MakesCallToMakeSelection() throws Exception {		
		this.mockMvc.perform(post("/vendingmachine")
				.param("actionType", "buyProduct"));				
		verify(vendingMachine).makeSelection();		
	}
		

	
}
