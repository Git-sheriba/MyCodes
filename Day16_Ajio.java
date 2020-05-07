package MyCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

public class Day16_Ajio {

	public static void main(String[] args) throws InterruptedException {
		
		String couponCodeEPIC = null;
		
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
//      1) Go to the URL https://www.ajio.com/shop/sale 		
		driver.get("https://www.ajio.com/shop/sale");
		
//	2) Enter Bags in the Search field and Select Bags in Women Handbags 
		WebElement webSearch = driver.findElementByXPath("//input[@placeholder='Search AJIO']");
		webSearch.click();
		webSearch.sendKeys("Bag",Keys.SPACE);
		driver.findElementByXPath("(//span[text()='Women Handbags'])[1]").click();
		
//      3) Click on five grid and Select SORT BY as "What's New"
		driver.findElementByXPath("//div[@class='five-grid']").click();
		
		WebElement webDropDown = driver.findElementByXPath("//div[@class='filter-dropdown']/select");
		Select drp = new Select(webDropDown);
		drp.selectByValue("newn");
		
//      4) Enter Price Range Min as 2000 and Max as 5000
		driver.findElementByXPath("//span[text()='price']").click();
		
		WebElement minPrice = driver.findElementById("minPrice");
		minPrice.click();
		minPrice.sendKeys("2000");
		
		WebElement maxPrice = driver.findElementById("maxPrice");
		maxPrice.click();
		maxPrice.sendKeys("5000");
		
		driver.findElementByXPath("(//button[@type='submit'])[2]").click();
		Thread.sleep(2000);
//5) Click on the product "Puma Ferrari LS Shoulder Bag"
		WebElement ferrariBag = driver.findElementByXPath("//div[text()='Ferrari LS Shoulder Bag']");
		Thread.sleep(2000);
		Actions builder = new Actions(driver);
		builder.moveToElement(ferrariBag);
		Thread.sleep(2000);
		ferrariBag.click();
//6) Verify the Coupon code for the price above 2690 is applicable for your product, 
//if applicable the get the Coupon Code and Calculate the discount price for the coupon
		Set<String> winSet1 = driver.getWindowHandles();
		List<String> winList1 = new ArrayList<String>(winSet1);
		driver.switchTo().window(winList1.get(1));
		String webprice = driver.findElementByXPath("//div[@class='prod-sp']").getText();
		System.out.println("Price:"+webprice);
		String price = webprice.replaceAll("\\D", "");
		int priceValue = Integer.parseInt(price);
		
		if(priceValue>=2690) {
			String webcouponCodeEPIC = driver.findElementByXPath("//div[@class= 'promo-title']").getText();
			couponCodeEPIC = webcouponCodeEPIC.substring(8);
			System.out.println("Coupon Code is :"+couponCodeEPIC);
		}
		String newPrice = driver.findElementByXPath("//div[@class='promo-discounted-price']/span").getText();
		int newPriceValue = Integer.parseInt(newPrice.replaceAll("\\D", ""));
		System.out.println("The new Price Value is:" +newPriceValue);
		System.out.println("The original Price value is :"+priceValue);
		int discountPrice = priceValue-newPriceValue;
		System.out.println("The discount price:"+discountPrice);
		
//7) Check the availability of the product for pincode 560043, print the expected delivery date if it is available
		driver.findElementByXPath("//span[text()='Enter pin-code to know estimated delivery date.']").click();
		WebElement pinCode = driver.findElementByXPath("//input[@name='pincode']");
		pinCode.click();
		pinCode.sendKeys("560043");
		driver.findElementByXPath("//button[text()='CONFIRM PINCODE']").click();
		
		WebElement notDelivered = driver.findElementByXPath("//div[@class='edd-pincode-msg-details edd-pincode-msg-details-not-deliverable']");
		if(notDelivered.isDisplayed()) {
			System.out.println("Delivery is not available for the given PIN code currently");
		}
		else {
			String deliverydate = driver.findElementByXPath("//ul[@class='edd-message-success-details']//span").getText();
			System.out.println("Deliverydate is:"+deliverydate);
		}
		
//8) Click on Other Informations under Product Details and Print the Customer Care address, phone and email 
		driver.findElementByXPath("//div[text()='Other information']").click();
		String customerDetails = driver.findElementByXPath("(//span[text()='Customer Care Address'])/following-sibling::span[2]").getText();
		System.out.println("Customer Details: \n"+customerDetails);
		
//9) Click on ADD TO BAG and then GO TO BAG 
		driver.findElementByXPath("//span[text()='ADD TO BAG']").click();
		Thread.sleep(8000);
		driver.findElementByXPath("//span[text()='GO TO BAG']").click();
		
//10) Check the Order Total before apply coupon 
		
		String orderTotalString = driver.findElementByXPath("//section[@id='orderTotal']//span[2]").getText();
		
		String str1 = orderTotalString.replaceAll("Rs.", "");
		System.out.println(str1);
		String str2 = str1.replaceAll(",", "");
		System.out.println(str2);
		double orderTotalDecimal = Double.parseDouble(str2);
		
		System.out.println("The Price Value:"+priceValue);
		System.out.println("OrderTotal is:"+orderTotalString);
		
		if(priceValue==(int)orderTotalDecimal) {
			System.out.println("Order Value is verified to be correct");
		}
		else {
			System.out.println("Please check for the order value");
		}
//11) Enter Coupon Code and Click Apply 
		WebElement webCouponCode = driver.findElementById("couponCodeInput");
		webCouponCode.click();
		webCouponCode.sendKeys(couponCodeEPIC);
		driver.findElementByXPath("//button[text()='Apply']").click();
		
//12) Verify the Coupon Savings amount(round off if it in decimal)
//under Order Summary and the matches the amount calculated in Product details 
		String summaryCouponSavings = driver.findElementByXPath("(//span[@class='price-value discount-price'])[2]").getText();
		System.out.println("Summary Coupon Savings Amount is :"+summaryCouponSavings);
		String num = summaryCouponSavings.replaceAll("Rs.","");
		double amount = Double.parseDouble(num);
		
		int couponSavingsAmount = (int) Math.round(amount);

		if(couponSavingsAmount==discountPrice)
			System.out.println("Order Summary Amount matches the Product details amount");
		else
			System.out.println("Order Summary Amount doesnot matche the Product details amount");
		
//13) Click on Delete and Delete the item from Bag 
		driver.findElementByXPath("//div[text()='Delete']").click();
		driver.findElementByXPath("//div[text()='DELETE']").click();
		
//14) Close all the browsers
		driver.quit();
	}

}
