package MyCodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Day10_JustDial2 {
	static ChromeDriver driver;
	static Day10_JustDial2 obj = new Day10_JustDial2();
	public static void main(String[] args) throws InterruptedException, IOException {

		float ratingValue = 0;

		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_ver81.exe");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");

		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
		options.merge(cap);
		driver = new ChromeDriver(options);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		driver.get("https://www.justdial.com/Chennai/Car-Repair-Services-Hyundai-Xcent/nct-11293522");
		Thread.sleep(5000);

		driver.findElementByXPath("//section[@id='best_deal_div']//span[1]").click();
		Thread.sleep(2000);

		List<WebElement> webRatings = driver.findElements(By.xpath("//span[@class='green-box']"));
		List<String> storeNameList = new ArrayList<String>();
		List<Integer> entryNumOfstoreList = new ArrayList<Integer>();
		List<String> phoneNumberList = new ArrayList<String>();

		for (int i = 0; i < webRatings.size(); i++) {
			String ratingNoString = webRatings.get(i).getText(); // Get the ratings
			ratingValue = Float.parseFloat(ratingNoString);
			if (ratingValue >= 4.5) { // Check for ratings>=4.5
				String webVotes = driver.findElementByXPath("(//span[@class='green-box'])[" + (i + 1)
						+ "]/following-sibling::span[contains(text(),'Votes')]").getText();
				String trimmedVotes = webVotes.trim();
				int VoteValue = Integer.parseInt(trimmedVotes.replaceAll("\\D", ""));
				if (VoteValue >= 50) { // Check for Votes>=50
					String webCenterName = driver
							.findElementByXPath("(//span[@class='lng_cont_name'])[" + (i + 1) + "]").getText();
					//System.out.println(" element[" + i + "]:" + webCenterName);
					entryNumOfstoreList.add(i);
					storeNameList.add(webCenterName);
				}
			}
		}
	//	JustDial2 obj = new JustDial2();
		phoneNumberList = obj.getEncodedPhoneNumber(storeNameList.size(), entryNumOfstoreList);
		obj.writeToExcel(storeNameList, phoneNumberList);
	}
	/*
	 * *getEncodedPhoneNumber() 
	 * int noOfStores : No of car repair stores with rating  >=4.5 and votes>=50;
	 * List<Integer> entryNumOfstoreList: Out of all the stores picked up, it
	 * gives the index number of all the stores retrieved based on the above condition
	 * 
	 * This method retrieves the Phone number of the store based on the index. The phone number is encoded
	 * in a particular pattern which is extracted and sent as a parameter to decodePhoneNumber().
	 */

	public List<String> getEncodedPhoneNumber(int noOfStores, List<Integer> entryNumOfstoreList) {
		String encodedString = null;
		String valueOfPhone = null;
		char[] concatenatedChar = new char[16];
		List<String> PhoneList = new ArrayList<String>();
		for (int j = 0; j < noOfStores; j++) {
			Integer index = entryNumOfstoreList.get(j);
		//	System.out.println(" Value of Store Index:" + index);
			for (int i = 1; i <= 16; i++) {
				String[] attribute = new String[i];

				// Gives the full class attribute
				attribute[i - 1] = driver
						.findElementByXPath("//*[@id='bcard" + index + "']/div[1]//p[2]/span/a//span[" + i + "]")
						.getAttribute("class");
				// get all the encoded values(last few characters after hyphen) of all the 16
				// digits.
				String[] splitString = attribute[i - 1].split("-");
				encodedString = splitString[1];

				//JustDial2 obj = new JustDial2();
				char decodedPhoneDigit = obj.decodePhoneNumber(encodedString);
				concatenatedChar[i - 1] = decodedPhoneDigit; // Store the digits in a char array
			}
			valueOfPhone = String.valueOf(concatenatedChar);
			PhoneList.add(valueOfPhone);
		}
		return PhoneList;
	}

	/*
	 * decodePhoneNumber(String encodedPhoneString)
	 * It accepts the encoded pattern of the phone number and decodes it to a String of a 16 digit Phone number
	 */
	public Character decodePhoneNumber(String encodedPhoneString) {
		Character decodedPhoneDigit = null;
		Map<String, Character> mapPhone = new LinkedHashMap<String, Character>();
		mapPhone.put("acb", '0');
		mapPhone.put("yz", '1');
		mapPhone.put("wx", '2');
		mapPhone.put("vu", '3');
		mapPhone.put("ts", '4');
		mapPhone.put("rq", '5');
		mapPhone.put("po", '6');
		mapPhone.put("nm", '7');
		mapPhone.put("lk", '8');
		mapPhone.put("ji", '9');
		mapPhone.put("dc", '+');
		mapPhone.put("fe", '(');
		mapPhone.put("hg", ')');
		mapPhone.put("ba", '-');

		if (mapPhone.containsKey(encodedPhoneString)) {
			decodedPhoneDigit = mapPhone.get(encodedPhoneString);
		}
		return decodedPhoneDigit;
	}

	/*
	 * writeToExcel()
	 * Accepts the list of Store names and its phone numbers and writes it into an excel.
	 */
	public void writeToExcel(List<String> storeName, List<String> phoneNumber) throws IOException {

		XSSFWorkbook wBook = new XSSFWorkbook(); // Open the workbook
		XSSFSheet sheet = wBook.createSheet("StoreDetails"); // Enter in to the sheet
		for (int i = 1; i <= storeName.size(); i++) {
			XSSFRow row = sheet.createRow(i);
			XSSFCell firstCell = row.createCell(0);
			XSSFCell SecondCell = row.createCell(1);
			firstCell.setCellValue(storeName.get(i - 1));
			SecondCell.setCellValue(phoneNumber.get(i - 1));
		}
		try {
			FileOutputStream out = new FileOutputStream(new File("./Data/JustDial.xlsx"));
			wBook.write(out);
			out.close();
			wBook.close();
			System.out.println(" Excel Sheet written successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end of WriteToExcel()

}
