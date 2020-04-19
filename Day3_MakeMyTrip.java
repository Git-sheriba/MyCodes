import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class makemytrip {

	public static void main(String[] args) throws InterruptedException {

		ChromeOptions option = new ChromeOptions();
		option.addArguments("--disable-notifications");

		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(option);

		WebDriverWait wait = new WebDriverWait(driver, 30);
		// 1) Go to https://www.makemytrip.com/
		driver.get("https://www.makemytrip.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		// 2) Click Hotels
		driver.findElementByXPath("//li[@class='menu_Hotels']").click();

		// 3) Enter city as Goa, and choose Goa, India
		driver.findElementByXPath("(//input[@type='text'])[1]").click();
		WebElement enterCity = driver.findElementByXPath("(//input[@placeholder='Enter city/ Hotel/ Area/ Building'])");
		enterCity.sendKeys("goa");
		wait.until(ExpectedConditions.elementToBeClickable(enterCity));
		Thread.sleep(2000);
		enterCity.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);

		// 4) Enter Check in date as Next month 15th (May 15) and Check out as start
		// date+5
		WebElement webCheckIn = driver.findElementByXPath("(//div[@class='DayPicker-Body'])[2]//div[text()='15']");
		webCheckIn.click();
		String checkInDate = webCheckIn.getText();
		int checkInDateInteger = Integer.parseInt(checkInDate);
		int checkOutDate = checkInDateInteger + 5;
		String stringCheckOutDate = Integer.toString(checkOutDate);

		WebElement webCheckOut = driver
				.findElementByXPath("(//div[@class='DayPicker-Body'])[2]//div[text()='" + stringCheckOutDate + "' ]");
		webCheckOut.click();

		// 5) Click on ROOMS & GUESTS and click 2 Adults and one Children(age 12). Click
		// Apply Button.
		driver.findElementByXPath("//input[@data-cy='guest']").click();
		driver.findElementByXPath("//li[@data-cy='adults-2']").click();
		driver.findElementByXPath("//li[@data-cy='children-1']").click();
		driver.findElementByXPath("//button[@data-cy='submitGuest']").click();

		// 6) Click Search button

		driver.findElementById("hsw_search_button").click();

		// 7) Select locality as Baga
		WebElement webCity = driver.findElementByXPath("(//ul[@class='filterList'])[2]/li[4]");
		wait.until(ExpectedConditions.elementToBeClickable(webCity));
		// A black screen filled the complete window and so I had to click somewhere on
		// the page to get rid of it.
		driver.findElement(By.xpath("//body")).click();
		webCity.click();

		// 8) Select 5 start in Star Category under Select Filters
		WebElement webStar = driver.findElementByXPath("//label[text()='5 Star']");
		wait.until(ExpectedConditions.elementToBeClickable(webStar));
		// webStar.click();
		System.out.println("5 star identified ");
		Thread.sleep(2000);
		Actions builder = new Actions(driver);
		builder.moveToElement(webStar).click().build().perform();
		System.out.println("5 star clicked ");

		// 9) Click on the first resulting hotel and go to the new window
		driver.findElementByXPath("(//div[@class='listingRowOuter'])[1]").click();

		// 10) Print the Hotel Name
		String hotelName = driver.findElementByXPath("(//p[@id='hlistpg_hotel_name'])[1]/span").getText();
		System.out.println("Hotel Name :" + hotelName);

		// 11) Click MORE OPTIONS link and Select 3Months plan and close
		// When Hotel name is clicked, goes to a new window where the MORE OPTION is
		// clicked

		Set<String> winSet = driver.getWindowHandles();
		List<String> winList = new ArrayList<String>(winSet);
		driver.switchTo().window(winList.get(1));

		driver.findElementByXPath("//span[text()='MORE OPTIONS']").click();
		driver.findElementByXPath("//table[@class='tblEmiOption']//tr[2]/td[@class='textRight']/span").click();
		driver.findElementByXPath("//span[@class='close']").click();

		/*
		 * This hotel is not available for booking and doesn't give me the options
		 * further. So, I havent coded step 12 and 13 12) Click on BOOK THIS NOW 13)
		 * Print the Total Payable amount
		 */

		// 14) Close the browser ( all the pages)
		driver.quit();
	}

}
