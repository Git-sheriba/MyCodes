package MyCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day14_Zalando {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_ver81.exe");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");

		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
		options.merge(cap);

		ChromeDriver driver = new ChromeDriver(options);
		Actions builder = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://www.zalando.com/");
		Thread.sleep(2000);
//2) Get the Alert text and print it 
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		System.out.println(alertText);
//3) Close the Alert box and click on Zalando.uk 
		alert.dismiss();
		Thread.sleep(2000);
		driver.findElementByXPath("//a[@class='nav_link nav_link-gb']").click();
//4) Click Women--> Clothing and click Coat 
		driver.findElementByXPath("//span[text()='Women']").click();
		driver.findElementByXPath("//span[text()='Clothing']").click();
		driver.findElementByXPath("//a[@aria-label='Browse Coats by category']").click();

		try {
			Thread.sleep(500);
			wait.until(ExpectedConditions
					.visibilityOf(driver.findElementByXPath("//button[contains(text(),'That’s OK')]")));
			driver.findElementByXPath("//button[contains(text(),'That’s OK')]").click();
		} catch (StaleElementReferenceException e) {
			System.out.println("Pop up not clicked");
			driver.findElementByXPath("//button[contains(text(),'That’s OK')]").click();
		}
//5) Choose Material as cotton (100%) and Length as thigh-length 
		driver.findElementByXPath("//button[@aria-label='filter by Material']").click();
		driver.findElementByXPath("//span[text()='cotton (100%)']").click();
		driver.findElementByXPath("//button[@aria-label='apply the Material filter']").click();
		Thread.sleep(2000);
		driver.findElementByXPath("//button[@aria-label='filter by Length']").click();
		driver.findElementByXPath("//span[text()='thigh-length']").click();
		driver.findElementByXPath("//button[@aria-label='apply the Length filter']").click();
//6) Click on Q/S designed by MANTEL - Parka coat 
		Thread.sleep(2000);
		driver.findElementByXPath("//div[text()='MANTEL - Parka - navy']").click();
//7) Check the availability for Color as Olive and Size as 'M' 
		wait.until(ExpectedConditions
				.elementToBeClickable(driver.findElementByXPath("(//div[@class='pFccbT X8C95H'])[2]//img")));
		driver.findElementByXPath("(//div[@class='pFccbT X8C95H'])[2]//img").click();
		Thread.sleep(2000);
		driver.findElementByXPath("//div[@class='v9RC-i']//button").click();
		driver.findElementByXPath("(//ul[@aria-labelledby='picker-trigger'])//li[3]").click();
//8) If the previous preference is not available, check  availability for Color Navy and Size 'M'
		String outOfStock = driver.findElementByXPath("//h2[text()='Out of stock']").getText();
		if (outOfStock.equalsIgnoreCase("Out of stock")) {
			System.out.println(outOfStock);
			driver.findElementByXPath("(//div[@class='pFccbT X8C95H'])[2]//img").click();
			Thread.sleep(2000);
			driver.findElementByXPath("(//button[@id='picker-trigger'])").click();
			driver.findElementByXPath("(//ul[@aria-labelledby='picker-trigger'])//li[3]").click();
		}
//9) Add to bag only if Standard Delivery is free 
		String freeDelivery = driver.findElementByXPath("(//button[@aria-label='Free'])[1]").getText();
		if (freeDelivery.equalsIgnoreCase("Free")) {
			driver.findElementByXPath("//span[text()='Add to bag']").click();
		}
//10) Mouse over on Your Bag and Click on "Go to Bag" 
		WebElement addToBag = driver.findElementByXPath(" (//div[contains(@class,'navToolItem-bag')]//div)[1]");
		builder.moveToElement(addToBag).perform();
		driver.findElementByXPath("//div[@class='z-1-button__content']").click();
		driver.findElementByXPath("//div[@data-id='delivery-estimation']").getText();
//11) Capture the Estimated Deliver Date and print 
		String estimation = driver.findElementByXPath("//div[@data-id='delivery-estimation']").getText();
		System.out.println("Estimated Deliver Date :" + estimation);
//12) Mouse over on FREE DELIVERY & RETURNS*, get the tool tip text and print
		WebElement toolTip = driver.findElementByXPath("//a[text()='Free delivery & returns*']");
		builder.moveToElement(toolTip).perform();
		String toolTipTitle = driver.findElementByXPath("//div[@class='z-navicat-header-uspBar_message']//span[2]")
				.getAttribute("title");
		System.out.println("toolTipTitle is:" + toolTipTitle);
//13) Click on FREE DELIVERY & RETURNS 
		toolTip.click();
//14) Click on Start chat in the Start chat and go to the new window 
        // Opening a new window
		driver.findElementByXPath("(//div[@class='channel__button'])[1]").click();
		Set<String> winSet = driver.getWindowHandles();
		List<String> winList = new ArrayList<String>(winSet);
		System.out.println(winList.size());
		driver.switchTo().window(winList.get(0));
//15) Enter you first name and a dummy email and click Start Chat 
		driver.findElementById("prechat_customer_name_id").sendKeys("Harini");
		driver.findElementById("prechat_customer_email_id").sendKeys("har@gmail.com");
		driver.findElementById("prechat_submit").click();
//16) Type Hi, click Send and print thr reply message and close the chat window.
        driver.findElementByXPath("//textarea[@class='liveAgentChatTextArea']").sendKeys("Hi");
        String zalandoMsg = driver.findElementByXPath("(//span[@class='operator'])[3]//span[2]").getText();
        System.out.println("zalandoMsg:"+zalandoMsg);
        driver.close();
	}
}
