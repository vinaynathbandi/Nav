package ApodGUI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class AddSchemaToTheViewlet 
{
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String EMS_WGS_INDEX;
	static String EMS_WGSNAME;
	static String SelectTopicName;
	static String DeleteDurableName;

	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		WGSName =Settings.getWGSNAME();
		UploadFilepath =Settings.getUploadFilepath();
		EMS_WGS_INDEX =Settings.getEMS_WGS_INDEX();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		SelectTopicName = Settings.getSelectTopicName(); 
		DeleteDurableName =Settings.getDeleteDurableName();
	}
	
	@Test
	@Parameters({"sDriver", "sDriverpath"})
	public static void Login(String sDriver, String sDriverpath) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);
		}
		
		//Login
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId=316)
	@Test(priority=1)
	public static void AddNewDashboard(String Dashboardname, ITestContext context) throws Exception
	{
		
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		//Check box selection
		driver.findElement(By.id("createInitialViewlets")).click();
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));
			
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(1000);
		
		if(driver.getPageSource().contains(Dashboardname))
		{
			System.out.println("Dash board is created");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Dashboard is created successfully");
		}
		else
		{
			System.out.println("Dash board is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create Dashboard");
			driver.findElement(By.xpath("Not created")).click();
		}
	 }
	
	
	@Parameters({"Attribute", "AttributeFirstValue", "SchemaName"})
	@TestRail(testCaseId=317)
	@Test(priority=2)
	public static void AddSchema(String Attribute, String AttributeFirstValue, String SchemaName, ITestContext context) throws InterruptedException
	{
		String AttributeValues=Attribute;
		String[] AttributeLoop = AttributeValues.split(",");
				
		driver.findElement(By.cssSelector("img[title=\"Manage viewlet schemas\"]")).click();
		driver.findElement(By.cssSelector("button.g-btn-blue-style")).click();
		
		//Creating schema
		driver.findElement(By.name("name")).sendKeys(SchemaName);
		
		
		//System.out.println(AttributeNames);
		for (String FinalValues : AttributeLoop)
		{
		driver.findElement(By.cssSelector("td[title=\""+ FinalValues +"\"]")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[8]")).click();
		Thread.sleep(1000);
		}
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Verification
		String data=driver.findElement(By.xpath("//tbody")).getText();
		System.out.println("Schema names:" +data);
		
		if(data.contains(SchemaName))
		{
			System.out.println("Schema is added successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Schema is added successfully");
			
		}
		else
		{
			System.out.println("Schema is not added successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faled to add Schema");
			driver.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-mod-manage-schemas/div/div[2]/button[2]")).click();
			driver.findElement(By.xpath("verfication failed"));
		}
		
		driver.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"SchemaName"})
	@TestRail(testCaseId=318)
	@Test(priority=3)
	public static void DeleteSchema(String SchemaName, ITestContext context) throws Exception
	{
		driver.findElement(By.cssSelector("img[title=\"Manage viewlet schemas\"]")).click();
		//Delete procedure
		driver.findElement(By.xpath("//button[4]")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(4000);
		
		//verification
		String data=driver.findElement(By.cssSelector("select.select-options")).getText();
		System.out.println(data);	
		
		if(data.contains(SchemaName))
		{
			System.out.println("Schema is not deleted");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete Schema");
			driver.findElement(By.xpath("Schema deletion failed"));
						
		}
		else
		{
			System.out.println("Schema is Deleted Successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Schema is Deleted successfully");
			
		}	
	}
	
	@Test(priority=4)
	@TestRail(testCaseId=319)
	public void AddAllAttributesButton(ITestContext context) throws InterruptedException
	{
		//Click on Manage viewlets Schema icon
		driver.findElement(By.cssSelector("img[title=\"Manage viewlet schemas\"]")).click();
		driver.findElement(By.cssSelector("button.g-btn-blue-style")).click();
		
		//Click on Add All button
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button")).click();
		
		String AvailableAttributes=driver.findElement(By.xpath("//div[2]/div/table/tbody")).getText();
		System.out.println(AvailableAttributes);
		
		if(AvailableAttributes.equalsIgnoreCase(""))
		{
			System.out.println("Add All button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Add All button option is working fine");
		}
		else
		{
			System.out.println("Add All button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Add All button option is not working properly");
			driver.findElement(By.xpath("Add All Attribute buttion is failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"DisplayedAttributesAfterRemoving"})
	@TestRail(testCaseId=320)
	@Test(priority=5)
	public void RemoveAllAttributesButton(String DisplayedAttributesAfterRemoving, ITestContext context) throws InterruptedException
	{
		//Click on Remove all button
		driver.findElement(By.xpath("//button[4]")).click();
		Thread.sleep(2000);
		
		//Store the Displayed attributes field data into string
		String DisplayedAttributes=driver.findElement(By.xpath("//div[3]/table/tbody")).getText();
		System.out.println("Data after click on Remove All: " +DisplayedAttributes);
		
		if(DisplayedAttributes.equalsIgnoreCase(DisplayedAttributesAfterRemoving))
		{
			System.out.println("Remove all button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Remove all button option is working fine");
		}
		else
		{
			System.out.println("Remove all button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Remove all button option is not working properly");
			driver.findElement(By.xpath("Remove All Attribute buttion is failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"AttributeName"})
	@TestRail(testCaseId=321)
	@Test(priority=6)
	public void AddAttributesButton(String AttributeName, ITestContext context) throws InterruptedException
	{
		//Send the Attribute name
		driver.findElement(By.cssSelector("td[title=\""+ AttributeName +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
		
		String DisplayedAttribute=driver.findElement(By.xpath("//div[3]/table/tbody")).getText();
		
		if(DisplayedAttribute.contains(AttributeName))
		{
			System.out.println("Add Button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Add button option is working fine");
		}
		else
		{
			System.out.println("Add Button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Add button option is not working properly");
			driver.findElement(By.xpath("Add button is not working")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"DisplayedAttributesAfterRemovingAttribute"})
	@TestRail(testCaseId=322)
	@Test(priority=7)
	public void RemoveAttributeButton(String DisplayedAttributesAfterRemovingAttribute, ITestContext context) throws InterruptedException
	{
		//Click on Attribute 
		driver.findElement(By.xpath("//div[3]/table/tbody/tr/td")).click();
		
		//click on remove button
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(2000);
		
		//Store the Displayed attributes field data into string
		String DisplayedAttributes=driver.findElement(By.xpath("//div[3]/table/tbody")).getText();
		System.out.println("Data After click on Remove: " +DisplayedAttributes);
		
		if(DisplayedAttributes.equalsIgnoreCase(DisplayedAttributesAfterRemovingAttribute))
		{
			System.out.println("Remove button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Remove button option is working fine");
		}
		else
		{
			System.out.println("Remove button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Remove button option is not working properly");
			driver.findElement(By.xpath("Remove Attribute buttion is failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Test(priority=8)
	@TestRail(testCaseId=323)
	public void MoveToTopButton(ITestContext context) throws InterruptedException
	{
		//Click on Add All button
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button")).click();
		
		//Select a Attribute
		driver.findElement(By.xpath("//tr[6]/td")).click();
		
		//store the Selected attribute into string
		String AttributeName=driver.findElement(By.xpath("//tr[6]/td")).getText();
		
		//Click on Move to top Button
		driver.findElement(By.xpath("//div[4]/button")).click();
		
		//Store the Top Attribute value into string
		String Top=driver.findElement(By.xpath("//td")).getText();
		
		if(AttributeName.equalsIgnoreCase(Top))
		{
			System.out.println("Move to top button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Move to top button option is working fine");
		}
		else
		{
			System.out.println("Move to top button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Move to top button option is not working properly");
			driver.findElement(By.xpath("Move to top button failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=9)
	@TestRail(testCaseId=324)
	public void MoveToBottomButton(ITestContext context) throws InterruptedException
	{
		//Select a Attribute
		driver.findElement(By.xpath("//tr[6]/td")).click();
		
		//store the Selected attribute into string
		String AttributeName=driver.findElement(By.xpath("//tr[6]/td")).getText();
		System.out.println(AttributeName);
		
		//click on move to bottom button
		driver.findElement(By.xpath("//div[4]/button[2]")).click();
		
		//Store the bottom Attribute into string
		String BottomAttribute=driver.findElement(By.xpath("//tr[63]/td")).getText();
		System.out.println(BottomAttribute);
		
		if(BottomAttribute.equalsIgnoreCase(AttributeName))
		{
			System.out.println("Move to Bottom button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Move to bottom button option is working fine");
		}
		else
		{
			System.out.println("Move to Bottom button is working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Move to bottom button option is not working properly");
			driver.findElement(By.xpath("Move to bottom button is failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=10)
	@TestRail(testCaseId=325)
	public void MoveUpButton(ITestContext context) throws InterruptedException
	{
		//Select a Attribute
		driver.findElement(By.xpath("//tr[6]/td")).click();
		Thread.sleep(2000);
		
		//store the Selected attribute into string
		String AttributeName=driver.findElement(By.xpath("//tr[6]/td")).getText();
		System.out.println(AttributeName);
		
		//Click on Move up button
		driver.findElement(By.xpath("//div[4]/div/button")).click();
		Thread.sleep(2000);
		
		//Store the attribute into string
		String AboveAttribute=driver.findElement(By.xpath("//tr[5]/td")).getText();
		System.out.println(AboveAttribute);
		
		if(AboveAttribute.equalsIgnoreCase(AttributeName))
		{
			System.out.println("Move up button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Move up button option is working fine");
		}
		else
		{
			System.out.println("Move up button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Moveup button option is not working properly");
			driver.findElement(By.xpath("Move Up button failed")).click();	
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=11)
	@TestRail(testCaseId=326)
	public void MoveDownButton(ITestContext context) throws InterruptedException
	{
		//Select a Attribute
		driver.findElement(By.xpath("//tr[6]/td")).click();
		Thread.sleep(2000);
		
		//store the Selected attribute into string
		String AttributeName=driver.findElement(By.xpath("//tr[6]/td")).getText();
		System.out.println(AttributeName);
		
		//click on Move down button
		driver.findElement(By.xpath("//div[4]/div/button[2]")).click();
		Thread.sleep(2000);
		
		//Store the Below attribute into string
		String BelowAttributeName=driver.findElement(By.xpath("//tr[7]/td")).getText();
		System.out.println(BelowAttributeName);
		
		if(BelowAttributeName.equalsIgnoreCase(AttributeName))
		{
			System.out.println("Move Down Button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Move Down button option is working fine");
		}
		else
		{
			System.out.println("Move Down Button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Move Down button option is not working properly");
			driver.findElement(By.xpath("Move Down button failed")).click();
		}
		Thread.sleep(1000);
		
		//Click on Cancel buttons
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".g-button-red")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=12)
	public static void Logout() throws InterruptedException
	{
		//Delete the dashboard 
		try
		{
			driver.findElement(By.cssSelector(".active .g-tab-btn-close-block")).click();
			//driver.findElement(By.cssSelector(".fa-times")).click();
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			System.out.println("Dashboards are not present");
		}
		Thread.sleep(1000);	

		
		//Logout icon
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		
		System.out.println("result getStatus: " + result.getStatus());
		// System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);
			try {
				//Update attachment to testrail server
				int testCaseID=0;
				//int status=(int) result.getTestContext().getAttribute("Status");
				//String comment=(String) result.getTestContext().getAttribute("Comment");
				  if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
					{
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();
					
					
					
					TestRailAPI api=new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileUtils.copyFile(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileUtils.copyFile(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}

	}
}