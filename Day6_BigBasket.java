import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Day6_BigBasket {

	static ChromeDriver driver;

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_ver81.exe");
		driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

//		1) Go to https://www.bigbasket.com/ 
		driver.get("https://www.bigbasket.com/");

//		2) mouse over on  Shop by Category  
		WebElement webShopByCategory = driver.findElementByXPath("//a[text()=' Shop by Category ']");
		Actions builder = new Actions(driver);
		Thread.sleep(2000);
		builder.moveToElement(webShopByCategory).perform();

//		3)Go to FOODGRAINS, OIL & MASALA --> RICE & RICE PRODUCTS  
		WebElement webFoodGrains = driver.findElementByLinkText("Foodgrains, Oil & Masala");
		builder.moveToElement(webFoodGrains).perform();

		WebElement webRiceProducts = driver.findElementByLinkText("Rice & Rice Products");
		builder.moveToElement(webRiceProducts).perform();

//   	4) Click on Boiled & Steam Rice 
		WebElement webBoiledRice = driver.findElementByLinkText("Boiled & Steam Rice");
		webBoiledRice.click();

//		5) Choose the Brand as bb Royal
		WebElement bbRoyalClick = driver.findElementByXPath("(//span[@class='cr']/i)[3]");
		bbRoyalClick.click();
		Thread.sleep(2000);

//      6) Go to Ponni Boiled Rice - Super Premium and select 5kg bag from Dropdown 	      
		WebElement webdropDown = driver
				.findElementByXPath("(//button[@class='btn btn-default dropdown-toggle form-control'])[1]");
		webdropDown.click();

		Thread.sleep(1000);
		WebElement web5Kg = driver.findElementByXPath("(//a[@ng-click='vm.onProductChange(allProducts)'])[4]");
		web5Kg.click();

//		7) print the price of Rice
		String discountPrice = driver.findElementByXPath("(//span[@class='discnt-price'])[1]").getText();
		System.out.println(" Price of the rice is :" + discountPrice);

//		8) Click Add button 
		driver.findElementByXPath("(//button[@class='btn btn-add col-xs-9'])[1]").click();

//		9) Verify the success message displayed  

//		10) Type Dal in Search field and enter 
		driver.findElementByXPath("//input[@id='input']").sendKeys("dal", Keys.ENTER);

//		12) Go to Toor/Arhar Dal and select 2kg & set Qty 2 

		WebElement webToorDal = driver.findElementByXPath("(//i[@class='fa fa-caret-down'])[2]");
		webToorDal.click();

		WebElement web2kgDal = driver.findElementByXPath("(//a[@ng-click='vm.onProductChange(allProducts)'])[7]");
		web2kgDal.click();

		WebElement webQty = driver.findElementByXPath("(//input[@ng-model='vm.startQuantity'])[3]");
		webQty.clear();
		webQty.sendKeys("2");

//		13) Print the price of Dal 
		String dalDiscPrice = driver.findElementByXPath("(//span[@class='discnt-price'])[3]").getText();
		System.out.println("Dal Discount Price is :" + dalDiscPrice);

//		14) Click Add button 
		driver.findElementByXPath("(//button[@class='btn btn-add col-xs-9'])[3]").click();

		// Success message for the purchase of dal.
		WebElement dalATOSuccess = driver.findElementByXPath("//div[@class='toast-title']");
		if (dalATOSuccess.isDisplayed()) {
			driver.findElementByClassName("toast-close-button").click();
		}

		// Add Udhayam Toor Dhal
		driver.findElementByXPath("(//button[@class='btn btn-add col-xs-9'])[2]").click();

		// Success message for Toor Dal
		try {
			if (driver.findElementByXPath("//div[@class='toast-title']").isDisplayed()) {
				driver.findElementByClassName("toast-close-button").click();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

//		15) Mouse hover on My Basket  
		WebElement webMyBasket = driver.findElementByXPath("//a[@qa='myBasket']");
		builder.moveToElement(webMyBasket).perform();
		Thread.sleep(2000);

//		16) Validate  the Sub Total displayed for the selected items
		Day6_BigBasket obj = new Day6_BigBasket();
		obj.calcSubTotal();

//      17) Reduce the Quantity of Dal as 1  
		driver.findElementByXPath("(//button[@ng-click='vm.decreamentQuantity(cartItem);'])[2]").click();
		Thread.sleep(2000);

//	    18) Validate the Sub Total for the current items 
		obj.calcSubTotal();

//		19) Close the Browser
		driver.close();
	}

	/*
	 * public void calcSubTotal() This method calculates the subtotal based on the
	 * items purchased in MyBasket
	 */
	public void calcSubTotal() {
		float subTotal = 0;

		// To get the number of items listed in MyBasket(Say, 3 items)
		String cartItemCount = driver.findElementByXPath("//span[@id='totalNumberOfCartItems']").getText();
		cartItemCount = cartItemCount.replaceAll("[^\\d]", "");
		cartItemCount.trim();
		int cartItemCountInt = Integer.parseInt(cartItemCount);
		System.out.println("Cart Item Count : " + cartItemCountInt);

		// To find the subtotal of all the items purchased
		for (int i = 1; i < cartItemCountInt + 1; i++) {

			String[] priceOfEachItem = new String[i];
			float eachPriceInt[] = new float[i];
			// To get the quantity of each item in MyBasket - cartQntyofEachItem
			String cartQnty_MRP = driver.findElementByXPath("(//div[@ng-bind='cartItem.quantity_mrp'])[" + i + "]")
					.getText();
			char ch = cartQnty_MRP.charAt(0);
			int cartQntyofEachItem = Integer.parseInt(String.valueOf(ch));

			// To get the cost of each item-eachPriceInt[i - 1]
			String Price = driver.findElementByXPath("(//span[@qa='priceMB'])[" + i + "]").getText();
			priceOfEachItem[i - 1] = Price;
			eachPriceInt[i - 1] = Float.parseFloat(priceOfEachItem[i - 1]);
			System.out.println("Price of Item(" + i + ") in Integers: " + eachPriceInt[i - 1]);
			System.out.println("Quantity of this Item :" + cartQntyofEachItem);
			// Subtotal for all purchased items in MyBasket
			subTotal = subTotal + (cartQntyofEachItem * eachPriceInt[i - 1]);
		}
		System.out.println("Calculated SubTotal of all the products :" + subTotal);

		// Subtotal from MyBasket
		String webSubTotal = driver.findElementByXPath("//span[@ng-bind='vm.cart.cart_total']").getText();

		// Validation of calculated subtotal and WebPage subtotal
		if (subTotal == Float.parseFloat(webSubTotal)) {
			System.out.println("******The subtotal of the purchased items is correct*******");
		} else {
			System.out.println("Wrong subtotal of the purchased items");
		}

	}
}
