package com.qa.StepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.qa.Listeners.ExtentReportListener;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class Login extends ExtentReportListener {

	@Given("when user is on login screen")
	public void when_user_is_on_login_screen() {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest(Feature.class, "Login");
			test = test.createNode(Scenario.class, "Login");
			loginfo = test.createNode(new GherkinKeyword("Given"), "when user is on login screen");
			System.setProperty("webdriver.chrome.driver", "F:\\Selenium\\chrome EXE\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.get("https://system.netsuite.com/pages/customerlogin.jsp");
			String title = driver.getTitle();
		    System.out.println(title);
			Assert.assertEquals("NetSuite - Customer Login", title);
			driver.manage().window().maximize();
			loginfo.pass("opened browser and entered url");
		} catch (AssertionError | Exception e) {
			
			testStepHandle("FAIL", driver, loginfo, e);

		}

	}

	@Then("user enters username and password")
	public void user_enters_username_and_password() {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode(new GherkinKeyword("Then"), "user enters username and password");
			driver.findElement(By.xpath("//input[@id='userNam']")).sendKeys("sravan.k@tvarana.com");
			driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Tvarana@2020");
			loginfo.pass("entered username and password");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e);
		}
	}

	@Then("user clicks on submit button")
	public void user_clicks_on_submit_button() {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode(new GherkinKeyword("Then"), "user clicks on submit button");
			driver.findElement(By.xpath("//input[@id='submitButton']")).click();
			driver.getTitle();
			loginfo.pass("submitted the button");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e);
		}
	}
	
	@Then("^user enter security answer$")
	public void user_enter_security_answer() throws Throwable {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode(new GherkinKeyword("Then"), "user_enter_security_answer");
			// Handling the Security Question Page
			WebElement secQuestionEle = driver.findElement(By.xpath("//td[@class='smalltextnolink']//following-sibling::td"));
			String actQuestion = secQuestionEle.getText();
			String question1 = "What was your childhood nickname?", question2 = "What is your oldest sibling's middle name?", question3 = "In what city or town was your first job?", question4 = "What is the middle name of your oldest child?", question5 = "What school did you attend for sixth grade?";
			if(actQuestion.trim().equals(question1)) {
				driver.findElement(By.id("null")).sendKeys("nickname");
			}
			else if(actQuestion.trim().equals(question2)) {
				driver.findElement(By.id("null")).sendKeys("name");
			}
			else if(actQuestion.trim().equals(question3)) {
				driver.findElement(By.id("null")).sendKeys("job");
			}
			else if(actQuestion.trim().equals(question4)) {
				driver.findElement(By.id("null")).sendKeys("child");
			}
			else if(actQuestion.trim().equals(question5)) {
				driver.findElement(By.id("null")).sendKeys("grade");
			}
			
			driver.findElement(By.name("submitter")).click();
			
			loginfo.pass("submitted the security answer");
		}
		catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e);
		}
	}


}