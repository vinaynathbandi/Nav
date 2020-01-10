package ApodGUI;


import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
public class EMSManagerViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String EMS_WGSNAME;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "Managername"})
	@Test
	public void Login(String sDriver, String sDriverpath, String Dashboardname, String Managername) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		String filepath=System.getProperty("user.dir") + "\\" + DownloadPath;
		
		//Selecting the browser
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", filepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		driver=new ChromeDriver(options);
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new EdgeDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new FirefoxDriver();
		}
		
		//Enter the URL into the browser and Maximize the window
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);	
			
		//Create viewlet button
		driver.findElement(By.xpath("//form/div/button[2]")).click();
		Thread.sleep(2000);
		
		// ---- Creating Manager Viewlet ----
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
			
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(2)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Managername);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(EMS_WGSNAME);
		
		//Click on EMS Check box
		driver.findElement(By.id("ems")).click();
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
	}
	
	@Parameters({"SchemaName"})
	@TestRail(testCaseId=281)
	@Test(priority=9)
	public static void ShowObjectAttributes(String SchemaName, ITestContext context) throws InterruptedException
	{
		try {
		//Objects Verification
		EMSObjects obj=new EMSObjects();
		obj.ObjectAttributesVerification(driver, SchemaName, EMS_WGSNAME);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show Object Attributes option is working fine");
		}
		catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Object Attributes option is not working prperly, check details: "+ e.getMessage());
		}
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=282)
	@Test(priority=6)
	public static void AddToFavorites(String FavoriteViewletName,ITestContext context) throws InterruptedException
	{
		//Create favorite Viewlet
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		Select wgsdropdown1=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown1.selectByVisibleText(EMS_WGSNAME);
		
		//Submit
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		int Node_Name=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Node_Name=4;
		}
		//node names data storage  
		String NodeName=driver.findElement(By.xpath("//datatable-body-cell["+ Node_Name +"]/div/span")).getText();
		System.out.println("Node name is: " +NodeName);
		
		//----------- Add Manager to favorite viewlet -----------------
		//Select Add tofavorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(2000);
		
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".g-block-bottom-buttons > .g-button-blue")).click();
		Thread.sleep(2000);
		
		//Favorite viewlet data storing
		String Fav1=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Fav viewlet data is: " +Fav1);
		
		//Verification condition
		if(Fav1.contains(NodeName))
		{
			System.out.println("EMS Manager is added to Favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "EMS Manager is added to Favorite viewlet");
		}
		else
		{
			System.out.println("EMS Manager is not added to Favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add EMS Manager to Favorite viewlet");
			driver.findElement(By.id("Add EMS Manager to favorite failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Query", "Queue", "Queuename"})
	@TestRail(testCaseId = 785)
	@Test(priority=2)
	public static void EMSMQSCConsoleCommandOption(String Query, String Queue, String Queuename, ITestContext context) throws InterruptedException
	{
		//Select the Console option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("EMS Scripts"))).perform();
		driver.findElement(By.linkText("Console...")).click();
		Thread.sleep(6000);
		
		//Create a queue using query 
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).sendKeys(Queue + Queuename);
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
		Thread.sleep(6000);
		
		//Click on Clear button
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(3000);
		
		//Enter the Query and Click on Submit
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).clear();
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).sendKeys(Query);
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
		Thread.sleep(6000);
		
		//Store the Console output into string
		String ConsoleOutput=driver.findElement(By.xpath("//textarea")).getAttribute("value");
		System.out.println("Responce data is: " +ConsoleOutput);
				
		if(ConsoleOutput.contains(Queuename))
		{
			System.out.println("Query executed");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "MQSC console Query is executed");
		}
		else
		{
			System.out.println("Query Failed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "MQSC console query failed");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("Console Query failed")).click();
		}
		Thread.sleep(1000);				
	}
	
	@TestRail(testCaseId = 786)
	@Test(priority=3, dependsOnMethods= {"EMSMQSCConsoleCommandOption"})
	public void SaveMQSCConsoleResponceData(ITestContext context) throws InterruptedException
	{
		try
		{
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(4000);
			System.out.println("Responce data is saved");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "MQSC console Responce data saved");
		}
		catch (Exception e)
		{
			System.out.println("Responce data is not saved");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "MQSC console Responce data not saved");
			driver.findElement(By.id("Console save failed")).click();
		}
		
	}
	
	@TestRail(testCaseId = 787)
	@Test(priority=4, dependsOnMethods= {"EMSMQSCConsoleCommandOption"})
	public void ClearMQSCConsoleResponceData(ITestContext context) throws InterruptedException
	{
		//Clear data by using clear button 
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(4000);
		
		//Store the Console output into string after clearing the console data
		String ClearedConsoleOutput=driver.findElement(By.xpath("//textarea")).getAttribute("value");
		System.out.println(ClearedConsoleOutput);
		
		if(ClearedConsoleOutput.equalsIgnoreCase(""))
		{
			System.out.println("Console cleared");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "MQSC console cleared");
		}
		else
		{
			System.out.println("Console not cleared");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "MQSC console is nt cleared");
			
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("Console not cleared")).click();
			
		}
		
		//close the window
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(1000);
		
	}
	
	@Test(priority=10)
	public void Logout() throws Exception
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
		
		//Logout option
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
