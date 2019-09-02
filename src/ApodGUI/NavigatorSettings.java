package ApodGUI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class NavigatorSettings 
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
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "wgs", "Favwgs", "FavoriteViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String wgs, String Favwgs, String FavoriteViewletName) throws Exception
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
		
		//---------- Create New Dashboard ---
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(wgs);
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(6000);	
		
		//------------- Create Favorite Viewlet ---------
		
		//Add Favorite Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown.selectByVisibleText(Favwgs);
		
		//Submit
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div[2]/button[2]")).click();
		Thread.sleep(1000);
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(5000);
		
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=327)
	public static void ShowInActiveChannelsCheckbox(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		
		//Show Inactive channels check box
		WebElement Checkbox=driver.findElement(By.xpath("//input[@name='user-settings']"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		String ChanneleViewletData=driver.findElement(By.xpath("//div[@id='main-page']/div[2]")).getText();
		
		if(ChanneleViewletData.contains("Inactive"))
		{
			System.out.println("Inactive Check box is Working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Inactive Check box is Working fine");
		}
		else
		{
			System.out.println("Inactive Check box is not Working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Inactive Check box is not Working properly");
			driver.findElement(By.xpath("Checkbox not working")).click();
		}
		
		Thread.sleep(1000);
	}
	
	@Test(priority=2)
	@TestRail(testCaseId=328)
	public static void ShowEmptyQueuesCheckbox(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[2]"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		for(int i=5; i<=20; i++)
		{
		String Depth=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		int result = Integer.parseInt(Depth);     
		System.out.println("Result values are: "+result);
		if(result==0)
		{
			System.out.println("Show Empty Queues Check box is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Empty Queues Check box is working fine");
			break;
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Empty Queues Check box is not working properly");
			driver.findElement(By.xpath("Checkbox not working")).click();
		}
		
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=3)
	@TestRail(testCaseId=329)
	public static void ShowTemporaryDynamicQueues(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[3]"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		for(int i=3; i<=180; i++)
		{
		String Queuename=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getAttribute("innerText");
		System.out.println(Queuename);
		
		if(Queuename.contains("INQ") || Queuename.contains("HASH") || Queuename.contains("NSQ"))
		{
			System.out.println("Show Temporary Dynamic Queues Check box is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Temporary Dynamic Queues Check box is working fine");
			break;
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Temporary Dynamic Queues Check box is not working properly");
			//driver.findElement(By.id("Show Temporary Dynamic Queues Check box")).click();
		}
		}
	}
	
	@Parameters({"FavoriteViewletName", "objpath1", "objpath2"})
	@TestRail(testCaseId=330)
	@Test(priority=4)
	public static void ShowFullNamesForFavoriteShortcuts(String FavoriteViewletName, String objpath1, String objpath2, ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[4]"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		//Select Add to favorite option in queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(2000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		//fav.selectByIndex(0);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		String FullNameForFavorite=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		//System.out.println(FullNameForFavorite);
		
		if(FullNameForFavorite.contains(objpath1) || FullNameForFavorite.contains(objpath2))
		{
			System.out.println("Show Full name for Favorite Short cuts is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Full name for Favorite Short cuts is working fine");
		}
		else
		{
			System.out.println("Show Full name for Favorite Short cuts is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Full name for Favorite Short cuts is not working properly");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"objpath1", "objpath2"})
	@TestRail(testCaseId=331)
	@Test(priority=5)
	public static void ShowFullNamesForSearchResultsObjects(String objpath1, String objpath2, ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		
		//Show Empty Queues check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[5]"));
		if(Checkbox.isSelected())
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		String Queuename=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		//System.out.println(Queuename);
		
		if(Queuename.contains(objpath1) || Queuename.contains(objpath2))
		{
			System.out.println("Show Full name For Search results checkbox is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Full name For Search results checkbox is working fine");
		}
		else
		{
			System.out.println("Show Full name For Search results checkbox is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Full name For Search results checkbox is not working properly");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		Thread.sleep(1000);
	
	}
	
	@Test(priority=6)
	@TestRail(testCaseId=332)
	public static void ShowSystemObjectsOnly(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(2000);
		
		//Show Show managers for default schema check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[6]"));
		if(Checkbox.isSelected())
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		String ColumnValues=driver.findElement(By.xpath("//datatable-body")).getText();
	    //System.out.println(ColumnValues);
		
		 if(ColumnValues.contains("SYSTEM."))
		    {
		    	
				System.out.println("Show System Objects only checkbox is not working fine");
		    	context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Show System Objects only checkbox is not working properly");
		    	driver.findElement(By.xpath("Condition failed")).click();
		    	
		    }
		    else
		    {
		    	System.out.println("Show System Objects only checkbox is working fine");
		    	context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Show System Objects only checkbox is working fine");
				
				
		    }
		
		/*for(int i=2; i<=10; i++)
	    {
	    String ColumnValues=driver.findElement(By.xpath("//datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
	    System.out.println(ColumnValues);
	   // int result = Integer.parseInt(ColumnValues);
	    if(ColumnValues.contains("SYSTEM."))
	    {
	    	
			System.out.println("Show System Objects only checkbox is not working fine");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show System Objects only checkbox is not working properly");
	    	driver.findElement(By.xpath("Condition failed")).click();
	    	
	    }
	    else
	    {
	    	System.out.println("Show System Objects only checkbox is working fine");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show System Objects only checkbox is working fine");
			
			
	    }
	    }*/
	}
	
	@Parameters({"Manager3", "Manager4"})
	@TestRail(testCaseId=333)
	@Test(priority=7)
	public static void ShowObjectSearchresultsFromActiveManagersOnly(String Manager3, String Manager4, ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@name='user-settings']")).click();
		//driver.findElement(By.xpath("(//input[@name='user-settings'])[2]")).click();
		driver.findElement(By.xpath("(//input[@name='user-settings'])[3]")).click();
		driver.findElement(By.xpath("(//input[@name='user-settings'])[4]")).click();
		driver.findElement(By.xpath("(//input[@name='user-settings'])[5]")).click();
		Thread.sleep(1000);
		
		//Show Show managers for default schema check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[7]"));
		if(Checkbox.isSelected())
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		try
		{
		boolean verified=false;
		for(int i=1; i<=10; i++)
		{
		String Managernames=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ i +"]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("List of Managers: " +Managernames);
		
		if(Managernames.equalsIgnoreCase(Manager1) || Managernames.equalsIgnoreCase(Manager2))
		{
			verified=true;
			break;
		}
		
		}
		if(verified)
		{
			System.out.println("Show object search results for Active queue managers condition working");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show object search results for Active queue managers condition is working fine");
		}
		else
		{
			System.out.println("Show object search results for Active queue managers condition is not working fine");
			
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show object search results for Active queue managers condition not working properly");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception Occured");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show object search results for Active queue managers condition not working properly" +e.getMessage());
			driver.findElement(By.xpath("Condition failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=8)
	@TestRail(testCaseId=334)
	public static void ShowManagersForDefaultSchemas(ITestContext context) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("(//input[@name='user-settings'])[2]")).click();
		
		//Show Show managers for default schema check box
		WebElement Checkbox=driver.findElement(By.xpath("(//input[@name='user-settings'])[8]"));
		if(Checkbox.isSelected())
		{
			Checkbox.click();
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		else
		{
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		Thread.sleep(8000);
		
		boolean verified=false;
	    for(int i=3; i<=10; i++)
	    {
	    String Columnheading=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-header/div/div[2]/datatable-header-cell["+ i +"]/div")).getText();
	    System.out.println("List of coloums are: " +Columnheading);
	    
	    if(Columnheading.equalsIgnoreCase("Manager Name"))
		{
			verified=true;
			break;
		}
	    }
	    
	    if(verified)
	    {
	    	System.out.println("Show manager for Default schemas checkbox failed");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show manager for Default schemas checkbox failed");
	    	driver.findElement(By.xpath("Condition failed")).click();
	    }
	    else
	    {
	    	System.out.println("Show manager for Default schems checkbox function is working fine");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show manager for Default schems checkbox function is working fine");
	    }
	    
	    Thread.sleep(1000);   
	} 
	
	@Test(priority=9)
	public void Logout() throws InterruptedException 
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(5000);
		
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
