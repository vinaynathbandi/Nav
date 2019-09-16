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
public class ClusterQueueManager {
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

	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname) throws Exception
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
		driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=242)
    @Parameters({"Clustername"})
	public static void AddClusterQueueManagerViewlet(String Clustername, ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		//Create Service
		driver.findElement(By.cssSelector(".object-type:nth-child(11)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Clustername);
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(WGSName);
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		if(driver.getPageSource().contains(Clustername))
		{
			System.out.println("Cluster Queue Manager Viewlet is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Cluster Queue Manager Viewlet created successfully");
		}
		else
		{
			System.out.println("Cluster Queue Manager viewlet is not created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "failed to add cluster Queue Manager Viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
			
    }
	
	@Parameters({"schemaName"})
	@TestRail(testCaseId=243)
	@Test(priority=7)
	public static void ShowObjectAttributesForClusterQM(String schemaName, ITestContext context) throws InterruptedException
	{
		/*//Search with the name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("ABC.SYSTEM.DEFAULT.SERVICE");
    	Thread.sleep(2000);*/
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ObjectAttributesVerification(driver, schemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attribute option for cluster Queue Manager viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to show object attributes for cluster Queue Manager");
		}
	}
	
	
    @Test(priority=3)
    @TestRail(testCaseId=244)
    public static void ClusterQMEvents(ITestContext context) throws InterruptedException 
    {
    	//Select Events option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Events...")).click();
    	Thread.sleep(1000);
    	
    	//Events Popup page
		String Eventdetails=driver.findElement(By.xpath("//th")).getText();
		//System.out.println(Eventdetails);
						
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Cluster Queue Manager events are working fine");
		}
		else
		{
			System.out.println("Events page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Cluster Queue Manager events are not working properly");
			driver.findElement(By.xpath("Events failed")).click();
		}
						
		//Clicking on Events Count
		try 
		{
			if(driver.findElement(By.xpath("//td")).isDisplayed())
			{
				String Eventcount=driver.findElement(By.xpath("//td")).getText();
				System.out.println(Eventcount);
				driver.findElement(By.xpath("//td")).click();
				
				//Click on daignostic tab
				driver.findElement(By.xpath("//app-mod-event-details/div/div/div/button[2]")).click();
				
				//get the vents count and store the into string
				String DignosticCount=driver.findElement(By.xpath("//div/input")).getAttribute("value");
				System.out.println("Daignostic events count:" +DignosticCount);
				
				if(Eventcount.equalsIgnoreCase(DignosticCount))
				{
					System.out.println("Events count is matched");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Event Count is Matched and working fine");
					//Close the Event details page
					driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
				}
				else
				{
					System.out.println("Events count is not matched");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Got exception while opening events page, check details: ");
					driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
					driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
					driver.findElement(By.id("Events count failed")).click();
				}
				
			}
		}
		catch (Exception e)
		{
			System.out.println("Events are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events not found");
		}
				
		//Close the events popup page
		driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
	}
	
    @Parameters({"FavoriteViewletName"})
    @TestRail(testCaseId=245)
	@Test(priority=4)
	public static void AddToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
    	int ClusterQMName_Index=3;
    	if(!WGSName.contains("MQM"))
    	{
    		ClusterQMName_Index=4;
    	}
    	//Store the service name into string            //div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]
		String ClusterName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ ClusterQMName_Index +"]")).getText();
		System.out.println("Cluster Name: " +ClusterName);
		
		//Create favorite viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown.selectByVisibleText(WGSName);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);

		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[4]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Fav viewlet data: "+Favdata);
		//Verification of service added to favorite viewlet
		if(Favdata.contains(ClusterName))
		{
			System.out.println("Cluster Queue manager name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Cluster Queue Manager name is added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Cluster Queue manager name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "failed to add Cluster Queue Manager name to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);

	}
	
	
	@Test(priority=5)
	@TestRail(testCaseId=246)
	public static void CompareClusterQueueManagers(ITestContext context) throws InterruptedException
	{
		//Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(1000);
		
		//Verification of popup
		String CompareProcess=driver.findElement(By.cssSelector("span.name")).getText();
		System.out.println(CompareProcess);
				
		if(CompareProcess.equalsIgnoreCase("Compare"))
		{
			System.out.println("Process Compare page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Comparing Cluster Queue Manager name is working fine");
		}
		else
		{
			System.out.println("Process Compare page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to compare Cluster Queue Manager name");
			driver.findElement(By.xpath("Comparision failed")).click();
		}
		Thread.sleep(1000);
				
		//Differences only
		driver.findElement(By.cssSelector("div.differences > label.switch > span.slider.round")).click();
		Thread.sleep(1000);
		try
		{
		String difference1=driver.findElement(By.xpath("//tr[2]/td[2]")).getText();
		System.out.println("First value" +difference1);
		String difference2=driver.findElement(By.xpath("//tr[2]/td[3]")).getText();
		System.out.println("Second value" +difference2);
		
		if(!(difference1.isEmpty() && difference2.isEmpty()))
		{
		
		if(difference1.equalsIgnoreCase(difference2))
		{
			System.out.println("Popup showing the same values Differences");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Bridge comparision is working fine");
    		driver.findElement(By.xpath("Differences")).click();
		}
		else
		{
			System.out.println("Popup showing the Different values");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Showing the different values");
			
		}
		}
		else
		{
			System.out.println("Empty records");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Showing the different values");
		}
		}
		catch (Exception e) {
         // TODO Auto-generated catch block
         System.out.println("No differences between Names lists");
         context.setAttribute("Status",5);
			context.setAttribute("Comment", "Exception occured while comparing Cluster Queue Managers");
        }
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);	
	}	
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=247)
	@Test(priority=6, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleClusterQMs(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int ClusterQMName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ClusterQMName_Index=4;
		}
		
		//Store the Service names into string
		String ClusterName2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ ClusterQMName_Index +"]")).getText();
		System.out.println("Name2 : " +ClusterName2);
		String ClusterName3=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ ClusterQMName_Index +"]")).getText();
		System.out.println("Name3 : " +ClusterName3);
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[4]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Fav data is: " +Favdata);
		
		//Verification of serviceses added to favorite viewlet
		if(Favdata.contains(ClusterName2) && Favdata.contains(ClusterName3))
		{
			System.out.println("Multiple Cluster Queue Manager names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Cluster Queue Manager names are added to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Cluster Queue manager names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Multiple Cluster Queue Manager names to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}

	@Test(priority=11)
	public void Logout() throws InterruptedException 
	{
		//delete Durable Viewlet		
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
		
		//Logout
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
