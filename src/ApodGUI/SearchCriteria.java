package ApodGUI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
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
public class SearchCriteria 
{
	String Queue="";
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
	static String Manager1;
	static String Manager2;

	
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
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "LocalQueue"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String LocalQueue) throws Exception
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
			//driver=new FirefoxDriver();
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);
		}
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(2000);
		
		//---------- Create New Dashboard ---
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(wgs);
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(6000);	
		
		
		//--------- Create Queue viewlet-----------
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		//Create Route viewlet
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(LocalQueue);
		
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(1000);
		dd.selectByVisibleText(WGSName);
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"MessageData"})
	@Test(priority=1)
	public void PutAmessageIntoQueue(String MessageData) throws Exception
	{
		//Check Show Empty Queues check box
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		int QueueName=3;
		if(!WGSName.contains("MQM"))
		{
			QueueName=4;
		}
		
		//Get the Queue name
		Queue=driver.findElement(By.xpath("//datatable-body-cell["+ QueueName +"]/div/span")).getText();
		//System.out.println("Queue name is: " +Queue);
				
		//Select the put new message option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions PutMessagesMousehour=new Actions(driver);
		PutMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		
		//Select the number of messages
		driver.findElement(By.name("generalNumberOfMsgs")).click();
		driver.findElement(By.name("generalNumberOfMsgs")).clear();
		driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("1");
		
		//Put a message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(4000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			System.out.println("No Exception");
		}
			
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(2000);          
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);	
	}
	
	
	@Parameters({"SearchCriteriaName", "SearchCriteriaData"})
	@TestRail(testCaseId = 349)
	@Test(priority=2)
	public void AddSearchCriteriaCondition(String SearchCriteriaName, String SearchCriteriaData, ITestContext context) throws Exception
	{
		//Edit Viewlet page
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Search Criteria
		boolean FindMessagesCheckbox=driver.findElement(By.id("sc-find-messages-checkbox")).isSelected();
		if(FindMessagesCheckbox==false)
		{
			driver.findElement(By.id("sc-find-messages-checkbox")).click();
		}
		
		driver.findElement(By.cssSelector("div.right > div.g-text-and-input.line > button.btn-white-round")).click();
		Thread.sleep(1000);
		
		//Click on Add icon
		driver.findElement(By.xpath("//img[@alt='new']")).click();
		
		//Double click on Element
		Actions actionclick=new Actions(driver);
		WebElement DC=driver.findElement(By.xpath("//span[contains(.,'Double Click here to Write Message Criteria Name')]"));
		actionclick.doubleClick(DC).build().perform();
		
		driver.findElement(By.cssSelector(".active > .datatable-row-center .ng-star-inserted")).click();
		driver.findElement(By.cssSelector(".active > .datatable-row-center .ng-star-inserted")).sendKeys(SearchCriteriaName);
		
		//Click on Data enter icon
		driver.findElement(By.xpath("//img[@alt='Data']")).click();
		
		//Enter the search data
		driver.findElement(By.xpath("//div[2]/textarea")).sendKeys(SearchCriteriaData);
		
		//Click on Save button
		driver.findElement(By.xpath("//app-mod-data/div/div[2]/div/div/div/button")).click();
		
		//Click on save button in windo
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		
		//Click on Apply changes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		/*//Search criteria name
		driver.findElement(By.cssSelector(".input-group > .form-control")).sendKeys(SearchCriteriaName);
		driver.findElement(By.cssSelector("button.btn.btn-outline-secondary")).click();
		Thread.sleep(2000);
		
		//Add Message data
		driver.findElement(By.xpath("//td/input")).click();
		driver.findElement(By.xpath("//img[@alt='Data']")).click();
		Thread.sleep(2000);
		
		//Enter data
		driver.findElement(By.xpath("//app-mod-message-data-criteria/div/div[2]/input")).sendKeys(SearchCriteriaData);
		
		//Click on OK buttons
		driver.findElement(By.xpath("//app-mod-message-data-criteria/div/div[3]/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[5]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Queue);*/
		
		//Store the table body
		String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
		
		for(int j=0; j<=Queue.length(); j++)
		{
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
		
		if(ViewletData.contains(Queue))
		{
			System.out.println("Search criteria is working fine");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Search criteria working fine");
		}
		else
		{
			System.out.println("Search criteria is not working fine");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Search criteria not working fine");
    		driver.findElement(By.xpath("Search criteria failed")).click();
		}
		
	}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException 
	{
		//Logout
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
		
		//Logout option
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		//System.out.println("dir: " + dir);
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
