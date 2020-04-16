import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class Day1_Myntra {

	public static void main(String[] args) throws InterruptedException {
		ChromeOptions option = new ChromeOptions();
		option.addArguments("--disable-notifications");
		
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(option);
		
		
		// 1) Open https://www.myntra.com/
		driver.get("https://www.myntra.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// 2) Mouse over on WOMEN
		WebElement WebWomen = driver.findElementByXPath("(//div[@class='desktop-navLink'])[2]");
		Actions builder = new Actions(driver);
		builder.moveToElement(WebWomen).perform();
        // 3) Click Jackets & Coats
		driver.findElementByXPath("//a[text()='Jackets & Coats']").click();
		// 4) Find the total count of item (top) 
		String totalItems = driver.findElementByXPath("//span[@class='title-count']").getText();
		String count = totalItems.replaceAll("\\D", "");
		System.out.println("Total no of Jackets and Coats: " + count);
		int totalNoOfJacketAndCoat = Integer.parseInt(count);
        // 5) Validate the sum of categories count matches
		String jacketCount = driver.findElementByXPath("(//span[@class='categories-num'])[1]").getText();
		String coatCount = driver.findElementByXPath("(//span[@class='categories-num'])[2]").getText();
		int noOfJackets = Integer.parseInt(jacketCount.substring(1, 5));
		int noOfCoats = Integer.parseInt(coatCount.substring(1, 4));
		if (noOfJackets + noOfCoats == totalNoOfJacketAndCoat) {
			System.out.println("Count Matched");
		}

		// 6) Check Coats
		driver.findElementByXPath("(//label[@class='common-customCheckbox vertical-filters-label'])[2]").click();

		// 7) Click + More option under BRAND
		driver.findElementByXPath("//div[@class='brand-more']").click();

		// 8) Type MANGO and click checkbox
		WebElement webMango = driver.findElementByXPath("//input[@class='FilterDirectory-searchInput']");
		Thread.sleep(2000);
		webMango.sendKeys("MANGO");
		driver.findElementByXPath("(//label[@class=' common-customCheckbox'])").click();

		// 9) Close the popup
		driver.findElementByXPath("//span[@class='myntraweb-sprite FilterDirectory-close sprites-remove']").click();

		// 10) Confirm all the Coats are of brand MANGO
		int matchMangoCoats = 0;
		int noMatchMangoCoats = 0;
		List<WebElement> brandNameList = driver.findElementsByXPath("//h3[@class='product-brand']");
		System.out.println("Size of the list :" + brandNameList.size());
		for (WebElement eachElement : brandNameList) {
			if (eachElement.getText().equals("MANGO")) {
				matchMangoCoats++;
			} else
				noMatchMangoCoats++;
		}
		if (brandNameList.size() == matchMangoCoats)
			System.out.println("All are MANGO brand");
		else
			System.out.println("No of items not of MANGO brand :" + noMatchMangoCoats);

		// 11) Sort by Better Discount
		WebElement webSort = driver.findElementByClassName("sort-sortBy");
		builder.moveToElement(webSort).perform();
		driver.findElementByXPath("//ul[@class='sort-list']/li[3]").click();

		// 12) Find the price of first displayed item
		List<WebElement> webProductPrice = driver.findElementsByClassName("product-price");
		System.out.println(webProductPrice.size());
		String webPrice = webProductPrice.get(0).getText();
		System.out.println("webPrice" + webPrice);
		System.out.println(webPrice.substring(0, 3));
		int firstProductPrice = Integer.parseInt(webPrice.substring(4, 8));
		System.out.println("Price of the first product :" + firstProductPrice);

		// 13. Mouse over on size of the first item
		WebElement discPrice = driver.findElementByXPath("//span[@class='product-discountedPrice'][1]");
		builder.moveToElement(discPrice).perform();
		Thread.sleep(2000);

		// 14) Click on WishList Now
		discPrice.click();
		
		// Close Browser
		driver.quit();
	
	}

}
