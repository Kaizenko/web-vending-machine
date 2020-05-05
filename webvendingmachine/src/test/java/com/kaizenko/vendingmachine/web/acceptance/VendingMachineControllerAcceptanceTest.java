package com.kaizenko.vendingmachine.web.acceptance;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import com.kaizenko.vendingmachine.testcategory.BrowserTests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@Category(BrowserTests.class)
@Tag("BrowserTests")
@RunWith(Cucumber.class)
@CucumberOptions(strict=true, monochrome = true, plugin = {"pretty", "html:target/site/cucumber-pretty", "json:target/cucumber.json"},
			features = "src/test/resources/features/")
@ActiveProfiles("test")
public class VendingMachineControllerAcceptanceTest {

	
}

