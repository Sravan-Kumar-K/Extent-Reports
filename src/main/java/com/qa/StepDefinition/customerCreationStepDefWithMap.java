package com.qa.StepDefinition;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.qa.Listeners.ExtentReportListener;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class customerCreationStepDefWithMap extends ExtentReportListener {
	WebDriver driver;
	Actions action;
	@Given("^User login with \"([^\"]*)\" & \"([^\"]*)\"$")
	public void user_login_with(String username, String password) {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest(Feature.class, "Creation of multiple Customers in Netsuite");
			test = test.createNode(Scenario.class, "Test scenario for multiple customer creation");
			loginfo = test.createNode(new GherkinKeyword("Given"), "user_login_with");
			
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\Sravan Kumar\\Downloads\\chromedriver_win32\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get("https://system.netsuite.com/pages/customerlogin.jsp");
			driver.findElement(By.id("userName")).sendKeys(username);
			driver.findElement(By.id("password")).sendKeys(password);
			driver.findElement(By.id("submitButton")).click();
			
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
			
			// Change User role
			action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath("//*[@id=\"spn_cRR_d1\"]/a"))).build().perform();
			eleAvailability(driver, By.xpath("//*[@id=\"ns-header-menu-userrole-item6\"]/a"), 10);
			driver.findElement(By.xpath("//*[@id=\"ns-header-menu-userrole-item6\"]/a")).click();
			
			loginfo.pass("Login Successful");
		}
		catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e);
		}
		
	}

	@Then("^Create customers with the below data & save the customer$")
	public void create_customers_with_the_below_data_save_the_customer(DataTable customerData) throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode(new GherkinKeyword("Then"), "create_customers_with_the_below_data_save_the_customer");
			
			action = new Actions(driver);
			for(Map<String,String> data: customerData.asMaps(String.class, String.class)) {
				Thread.sleep(5000);
				eleAvailability(driver, By.xpath("//span[text()='Lists']"), 10);
				action.moveToElement(driver.findElement(By.xpath("//span[text()='Lists']"))).build().perform();
				eleAvailability(driver, By.xpath("//span[text()='Relationships']"), 10); // Explicit Wait
				action.moveToElement(driver.findElement(By.xpath("//span[text()='Relationships']"))).build().perform();
				eleAvailability(driver, By.xpath("//span[text()='Customers']"), 10); // Explicit Wait
				action.moveToElement(driver.findElement(By.xpath("//span[text()='Customers']"))).build().perform();
				eleAvailability(driver, By.xpath("//span[text()='New']"), 10);
				driver.findElement(By.xpath("//span[text()='New']")).click();
				
				// Change the Form to "Standard Customer Form"
				eleClickable(driver, By.xpath("//input[@id='inpt_customform4']"), 10);
				driver.findElement(By.xpath("//input[@id='inpt_customform4']")).click();
				List<WebElement> selectForm = driver.findElements(By.xpath("//div[@class='dropdownDiv']//div"));
				for(int i=0;i<selectForm.size();i++) {
					String formValue = selectForm.get(i).getText().trim();
					if(formValue.equals("Standard Customer Form")) {
						selectForm.get(i).click();
					}
				}
				Thread.sleep(5000);
				
				// Handling customer type & entering customer name
				String customerType = data.get("Type");
				String customerFirstName = data.get("First name");
				String customerLastName = data.get("Last name");
				String customerCompany = data.get("Company name");
				if(customerType.equals("COMPANY")) {
					driver.findElement(By.xpath("//div[@id='isperson_F']//div//span//span[@id='isperson_fs']//input")).click();
					// Enter company name
					driver.findElement(By.id("companyname")).sendKeys(customerCompany);
				}
				else {
					driver.findElement(By.xpath("//div[@id='isperson_T']//div//span//span[@id='isperson_fs']//input")).click();
					// Enter first name, last name
					driver.findElement(By.id("firstname")).sendKeys(customerFirstName);
					driver.findElement(By.id("lastname")).sendKeys(customerLastName);
					driver.findElement(By.id("companyname")).sendKeys(customerCompany);
				}
				
				// Enter Parent Company
				String customerParentCompany = data.get("Parent Company");
				driver.findElement(By.xpath("//span[@id='parent_actionbuttons_parent_fs']//a[2]")).click();
				driver.findElement(By.xpath("//div[@id='parent_fs_tooltipMenu']//a[1]")).click();
				Thread.sleep(5000);
				driver.findElement(By.id("st")).sendKeys(customerParentCompany);
				driver.findElement(By.id("Search")).click();
				Thread.sleep(5000);
				List<WebElement> parentList = driver.findElements(By.xpath("//div[@id='inner_popup_div']//table//tr//td//following-sibling::td//a"));
				for(int j=0;j<parentList.size();j++) {
					String parentCustomer = parentList.get(j).getText().trim();
					if(parentCustomer.equals("Sravan K")) {
						parentList.get(j).click();
						break;
					}
				}
				Thread.sleep(5000);
				// Click on save button
				driver.findElement(By.id("btn_secondarymultibutton_submitter")).click();
				eleAvailability(driver, By.xpath("//div[@class='descr']"), 10);
			    String confirmationMessage = driver.findElement(By.xpath("//div[@class='descr']")).getText().trim();
			    String customerName = driver.findElement(By.xpath("//div[@class='uir-record-name']")).getText();
			    if(confirmationMessage.equals("Customer successfully Saved")) {
			    	System.out.println("Customer '"+customerName+"' created successfully");
			    }
				
			}
			
			loginfo.pass("Customers Created Successfully");
		}
		catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e);
		}
		
	}

//	@Then("^close the browser$")
//	public void close_the_browser() {
//		ExtentTest loginfo = null;
//		try {
//			loginfo = test.createNode(new GherkinKeyword("Then"), "close_the_browser");
//			driver.close();
//			loginfo.pass("Browser closed Successfully");
//		}
//		catch (AssertionError | Exception e) {
//			testStepHandle("FAIL", driver, loginfo, e);
//		}
//	}
	
	public static void eleAvailability(WebDriver driver, By locator, int time) {
		new WebDriverWait(driver, time).until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public static void eleClickable(WebDriver driver, By locator, int time) {
		new WebDriverWait(driver, time).until(ExpectedConditions.elementToBeClickable(locator));
	}
	
}
