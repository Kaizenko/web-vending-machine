package com.kaizenko.vendingmachine.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kaizenko.vendingmachine.domain.Product;
import com.kaizenko.vendingmachine.service.VendingMachine;

@Controller
public class VendingMachineController {
	
	@Autowired
	VendingMachine vendingMachine;
	int change=0;
	String product="";
	
	 @GetMapping("/vendingmachine")
	 public String vendingMachineForm(Model model) {
		 model.addAttribute("payment", vendingMachine.getPayment());
		 model.addAttribute("message", vendingMachine.getMessage());
		 model.addAttribute("change", change);
		 model.addAttribute("product", product);
		 change=0;
		 product="";
	     return "vendingmachine";
	 }
	 
		 
	 @PostMapping(value="/vendingmachine", params= {"actionType"})
	 public String makePayment(@RequestParam String actionType, Model model) {
		 if (actionType.equals("makePayment")) {
			 vendingMachine.makePayment();
		 }
		 else if (actionType.equals("buyProduct")) {
			 Product productSelected=vendingMachine.makeSelection();
			 if (productSelected==null) {
				 product="";
			 }
			 else {
				 product="Yummy Product";
			 }
		 }
		 else {
			 change=vendingMachine.releaseChange();				 
		 }
		 return "redirect:/vendingmachine";
	 } 	 
}
