package MyCodes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day17_Azure {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		
//1  Go to https://azure.microsoft.com/en-in/
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_ver81.exe");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
		options.merge(cap);
		
		ChromeDriver driver = new ChromeDriver(options);
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);	
//1) Go to the URLhttps://azure.microsoft.com/en-in/ 		
		driver.get("https://azure.microsoft.com/en-in/");
//2) Click on Pricing 
		driver.findElementByLinkText("Pricing").click();
//3) Click on Pricing Calculator
		driver.findElementByXPath("//ul[@class='linkList initial-list']/li[2]").click();
//4) Click on Containers
		driver.findElementByXPath("//button[@data-event-property='containers']").click();
//5) Select Container Instances 
		driver.findElementByXPath("(//button[@title='Container Instances'])[2]").click();
//6) Click on Container Instance Added View 
		driver.findElementByLinkText("View").click();
//7) Select Region as "South India"
		WebElement webRegion = driver.findElementByXPath("//select[@aria-label='Region']");
	    Select drp = new Select(webRegion);
	    drp.selectByVisibleText("South India");
//8) Set the Duration as 180000 seconds 
	    WebElement webSeconds = driver.findElementByXPath("//input[@aria-label='Seconds']");
	    webSeconds.clear();
	    webSeconds.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT, Keys.END),"18000");

//9) Select the Memory as 4GB 
	    WebElement webMemory = driver.findElementByXPath("//select[@aria-label='Memory']");
	    Select drp1 = new Select(webMemory);
	    drp1.selectByVisibleText("4 GB");
	    
//10) Enable SHOW DEV/TEST PRICING 
	    driver.findElementByXPath("//span[text()='Show Dev/Test Pricing']").click();
//11) Select Indian Rupee  as currency
	    WebElement webCurrency = driver.findElementByXPath("(//select[@aria-label='Currency'])[1]");
	    Select drp2 = new Select(webCurrency);
	    drp2.selectByValue("INR");
//12) Print the Estimated monthly price 
	    String EstimatedAmount = driver.findElementByXPath("(//span[@class='numeric'])[4]").getText();
	    System.out.println("The Estimated Amount is :"+EstimatedAmount);
//13) Click on Export to download the estimate as excel 
	    driver.findElementByXPath("//button[@class='calculator-button button-transparent export-button']").click();
//14) Verify the downloded file in the local folder
        File f = new File("C:\\Users\\Jesus\\Downloads\\ExportedEstimate.xlsx");
        if (f.exists()) 
            System.out.println("Exists"); 
        else
            System.out.println("Does not Exists"); 
	
//15) Navigate to Example Scenarios and Select CI/CD for Containers 
	    WebElement webExampleScenario = driver.findElementByLinkText("Example Scenarios");
	    Actions builder = new Actions(driver);
	    builder.moveToElement(webExampleScenario).click().perform();
	    driver.findElementByXPath("//button[@title='CI/CD for Containers']").click();
	    
//16) Click Add to Estimate
	    WebElement webAddToEstimate = driver.findElementByXPath("//button[text()='Add to estimate']");
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click()", webAddToEstimate);
	    Thread.sleep(4000);
//17) Change the Currency as Indian Rupee 
	    WebElement webCurrency1 = driver.findElementByXPath("(//select[@aria-label='Currency'])[1]");
	    js.executeScript("arguments[0].click()", webCurrency1);
	    Select drp3 = new Select(webCurrency1);
	    Thread.sleep(2000);
	    drp3.selectByValue("INR");
	    Thread.sleep(2000);
//18) Enable SHOW DEV/TEST PRICING 
	    driver.findElementByXPath("//span[text()='Show Dev/Test Pricing']").click();
//19) Export the Estimate 
	    driver.findElementByXPath("//button[@class='calculator-button button-transparent export-button']").click();
	    System.out.println("4");
//20) Verify the downloded file in the local folder
        File f1 = new File("C:\\Users\\Jesus\\Downloads\\ExportedEstimate.xlsx");
        if (f1.exists()) 
            System.out.println("Exists"); 
        else
            System.out.println("Does not Exists");
	    
	}

}
