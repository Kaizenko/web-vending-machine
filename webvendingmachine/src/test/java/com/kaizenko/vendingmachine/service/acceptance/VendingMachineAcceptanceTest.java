package com.kaizenko.vendingmachine.service.acceptance;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class) 
@CucumberOptions(strict=true, monochrome = true, plugin = "pretty", features = "src/test/resources")
@ActiveProfiles("test")
public class VendingMachineAcceptanceTest {

	
}

