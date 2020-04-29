package com.kaizenko.vendingmachine.service.acceptance;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class) 
@CucumberOptions(plugin = "pretty", features = "src/test/resources")
public class VendingMachineAcceptanceTest {

	
}

