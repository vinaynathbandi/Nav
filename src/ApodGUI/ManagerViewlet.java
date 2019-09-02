package ApodGUI;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class ManagerViewlet 
{
	static WebDriver driver;
	static String IPAddress;
	static String HostName;
	static String PortNo;
	static String WGSPassword;
	static String Node_hostname;
	static String NodeNameFromIcon;
	static String HostNameFromIcon;
	static String IPAddressFromIcon;
	static String QueueManagerName;
	static String Node_Hostname;
	static String DefaultTransmissionQueue;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String M_QueueManagerName;
	static String WGSName;
	static String Dnode;

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		IPAddress = Settings.getIPAddress();
		HostName = Settings.getWGS_HostName();
		PortNo = Settings.getWGS_PortNo();
		WGSPassword = Settings.getWGS_Password();
		Node_hostname = Settings.getNode_Hostname();
		NodeNameFromIcon = Settings.getNode_NameFromIcon();
		HostNameFromIcon = Settings.getHostNameFromIcon();
		IPAddressFromIcon = Settings.getIPAddressFromIcon();
		QueueManagerName = Settings.getQueueManagerName();
		Node_Hostname =Settings.getNode_Hostname();
		DefaultTransmissionQueue =Settings.getDefaultTransmissionQueue();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		M_QueueManagerName =Settings.getM_QueueManagerName();
		WGSName =Settings.getWGSNAME();
		Dnode =Settings.getDnode();
	}

	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "Managername"})
	@Test
	public void Login(String sDriver, String sDriverpath, String Dashboardname, String Managername) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		//Selecting the browser
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", DownloadPath);
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
		Thread.sleep(6000);
		
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
		WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Description"})
	@TestRail(testCaseId = 47)
	@Test(priority=1)
	public static void AddNewManagerFromIcon(String Description, ITestContext context) throws InterruptedException
	{
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add Queue Manager']")).click();
		
		//WGS Selection
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(WGSName);
		Thread.sleep(2000);
		
		//Node selection
		driver.findElement(By.cssSelector(".ng-input > input")).click();
		
		try 
		{
			List<WebElement> ManagerNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(ManagerNode.size());	
			for (int i=0; i<ManagerNode.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + ManagerNode.get(i).getAttribute("id"));
				String s=ManagerNode.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=ManagerNode.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		//driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		
		/*Select Node=new Select(driver.findElement(By.xpath("//div[2]/input")));
		Node.selectByVisibleText("DESKTOP-E1JT2VR");
		Thread.sleep(2000);*/
	
		//Click on Select Path
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		
		//Queue Details
		driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(M_QueueManagerName);
		driver.findElement(By.xpath("//app-qmgrcreatestep1/div/div[4]/div/input")).sendKeys(DefaultTransmissionQueue);
		driver.findElement(By.xpath("//textarea")).sendKeys(Description);
		
		//Next button 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys("New Manager"); 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//Log Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Desktop");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//Data Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Test data path");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(2000);
		
		//Final Submit
		driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/button")).click();
		Thread.sleep(25000);
		
		try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e) 
		{
			System.out.println("error popup is not displayed");
		} 
		
		//Refresh the Viewlets
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(2000);
		
		/*driver.findElement(By.xpath("//div[3]/app-viewlet/div/div[2]/div/div[2]/div/div/img")).click();
		Thread.sleep(2000);*/
		
		//Get the Viewlets Data
		String viewlet1=driver.findElement(By.xpath("//datatable-body")).getText();
		
		
		//Verification condition 
		if(viewlet1.contains(M_QueueManagerName))
		{
			System.out.println("Queue Manager is successfully added");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue Manager added successfully");
		}
		else
		{
			System.out.println("Queue Manager is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add Queue Manager");
			driver.findElement(By.id("QM from icon is failed")).click();
		}
		Thread.sleep(4000);
	}
	
	@Parameters({"SchemaName", "Attributes"})
	@TestRail(testCaseId = 48)
	@Test(priority=24)
	public static void ShowObjectAttributes(String SchemaName, String Attributes,ITestContext context) throws InterruptedException
	{
		try {
		//Objects Verification
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ManagerAttributes(driver, SchemaName, Attributes, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Object attributes working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while showing object attributes, Check details: " + e.getMessage());
		}
	}
	
	@Test(priority=3)
	@TestRail(testCaseId = 49)
	public void ShowTopology(ITestContext context) throws InterruptedException
	{
		int ManagerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ManagerName_Index=4;
		}
		
		//Store the Manager name into string
		String ManagerName=driver.findElement(By.xpath("//datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
		
		//Select Show topology option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Topology")).click();
		Thread.sleep(1000);
		
		//Store the Topology page data into string
		String Topology=driver.findElement(By.cssSelector("svg")).getText();
		
		//Verification condition
		if(Topology.contains(ManagerName))
		{
			System.out.println("Topology page is opened with the selected Queue manager");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Topology page is opened with the selected Queue manager");
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
		} 
		else
		{
			System.out.println("Topology page is not opened with the selected Queue manager");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Topology page");
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			driver.findElement(By.xpath("Topology page failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Test(priority=4)
	@TestRail(testCaseId = 50)
	public static void StartAllWMQObjects(ITestContext context) throws InterruptedException
	{
		//Select the  Start All WMQ Objects from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li")).click();
		Thread.sleep(1000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Started all WMQ objects");
		//Click on Yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
	}
	
	@Test(priority=5)
	@TestRail(testCaseId = 51)
	public static void StopAllWMQObjects(ITestContext context) throws InterruptedException
	{
		//Select the  Stop All WMQ Objects from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[2]")).click();
		Thread.sleep(1000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Stopped all WMQ objects");
		//Click on yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
	
	}
	
	@Test(priority=6)
	@TestRail(testCaseId = 52)
	public void Security(ITestContext context) throws InterruptedException
	{
		//Select the Security from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Security...")).click();
		
		try {
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup page is not displayed");
		}
		Thread.sleep(1000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Security option is working fine");
		
		//Click on Cancel button
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=7)
	@TestRail(testCaseId = 53)
	public void ViewErrorLogs(ITestContext context) throws InterruptedException
	{
		try
		{
		//Select the ViewErrorLogs from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("View Error Log...")).click();
		Thread.sleep(1000);
		
		try
		{
			//Click on Log file name
			driver.findElement(By.xpath("//div[3]/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
			
			//Click on open
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Error log page is working fine");
		
		}
		catch (Exception e)
		{
			System.out.println("Error logs are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Error log page is working fine");
			
			//Close the Error log page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(1000);
		}
		
		//Close the Error log page
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(1000);
		
		}
		catch (Exception e)
		{
			System.out.println("Exception occured in View error log page");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured in View error log page, check details: "+ e.getMessage());
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			Thread.sleep(20000);
		}
		
	}
	
	@Test(priority=8)
	@TestRail(testCaseId = 54)
	public static void Properties(ITestContext context) throws InterruptedException
	{
		//Select properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(1000);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		Thread.sleep(1000);
		
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification
		if(FieldNamevalue == false)
		{
			 System.out.println("Manager Name field is UnEditable");
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Manager Name field is UnEditable, condition working fine");
				
			 driver.findElement(By.cssSelector(".btn-primary")).click();
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Condition failed, manager name is in editable");
			System.out.println("Manager Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Manager name edit function Failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Query"})
	@TestRail(testCaseId = 55)
	@Test(priority=9)
	public static void MQSCConsole(String Query,ITestContext context) throws InterruptedException
	{
		//Select the Console option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
		driver.findElement(By.linkText("Console...")).click();
		Thread.sleep(1000);
		
		//Enter the Query and Click on Submit
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(Query);
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div[2]/button")).click();
		
		//Store the Console output into string
		String ConsoleOutput=driver.findElement(By.xpath("//textarea")).getText();
		System.out.println(ConsoleOutput);
		
		if(ConsoleOutput.contains("QUEUE(NASTEL.EVENT.QUEUE)"))
		{
			System.out.println("Query executed");
		}
		Thread.sleep(1000);
		
		//------------ Save the Console data into file using save button ---------------
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);
		
		//------------Clear data by using clear button --------------
		driver.findElement(By.xpath("//div[2]/div/div/button")).click();
		Thread.sleep(1000);
		
		//Store the Console output into string after clearing the console data
		String ClearedConsoleOutput=driver.findElement(By.xpath("//textarea")).getText();
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
			
		}
		
		//close the window
		driver.findElement(By.xpath("//div[4]/button")).click();
		Thread.sleep(1000);
				
	}
	
	@TestRail(testCaseId = 56)
	@Test(priority=10)
	public static void DiscoverNow(ITestContext context) throws InterruptedException
	{
		try {
		//Select Incremental option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
		driver.findElement(By.linkText("Incremental")).click();
		Thread.sleep(1000);
				
		//Select Full option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverFull=new Actions(driver);
		MousehoverFull.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
		driver.findElement(By.linkText("Full")).click();
		Thread.sleep(2000);
		
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Discover now option working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an error while testing discover now option: "+ e.getMessage());
		}
		
	}
	
	@Parameters({"DeleteManagerName"})
	@TestRail(testCaseId = 57)
	@Test(priority=11)
	public void Delete(String DeleteManagerName, ITestContext context) throws InterruptedException
	{
		try {
		
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(DeleteManagerName);
		//Select Delete option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[8]")).click();
		Thread.sleep(1000);	
		
		//Click on confirmation Yes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Delete option working fine");
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an error while testing delete option: "+ e.getMessage());
		}
	}
	
	@Parameters({"DeleteManagerCheckboxValue", "QueueManagerName"})
	@TestRail(testCaseId = 58)
	@Test(priority=12)
	public void DeleteFromDB(int DeleteManagerCheckboxValue, String QueueManagerName, ITestContext context) throws InterruptedException
	{
		//Select Delete from Database option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[9]")).click();
		Thread.sleep(1000);
		
		//Store the manager viewlet data into string
		String ManagerData=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification condition
		if(ManagerData.contains(QueueManagerName))
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Manager is not deleted");
			System.out.println("Manager is not deleted");
			driver.findElement(By.xpath("manager not deleted")).click();
		}
		else
		{
			System.out.println("manager is deleted");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Manager is deleted, delete fron DB option working fine");
		}
		
	}
	
	@TestRail(testCaseId = 59)
	@Test(priority=13)
	public static void Events(ITestContext context) throws InterruptedException
	{
		//Select Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Events...")).click();
		Thread.sleep(4000);
		
		//Events Popup page
		String Eventdetails=driver.findElement(By.xpath("//th")).getText();
		//System.out.println(Eventdetails);
		
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Event page is opened and working fine");
		}
		else
		{
			System.out.println("Events page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open event options");
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
	@TestRail(testCaseId = 60)
	@Test(priority=14)
	public static void AddToFavorites(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Create favorite Viewlet
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		Select wgsdropdown1=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown1.selectByVisibleText(WGSName);
		
		//Submit
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		int ManagerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ManagerName_Index=4;
		}
		
		//Manager names data storage
		String Manager1=driver.findElement(By.xpath("//datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
		System.out.println(Manager1);
		
		//----------- Add Manager to favorite viewlet -----------------
		//Select Add tofavorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(2000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByIndex(0);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".g-block-bottom-buttons > .g-button-blue")).click();
		Thread.sleep(2000);
		
		//Favorite viewlet data storing
		String Fav1=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//String Fav2=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//|| Manager1.contains(Fav2)
		//Verification condition
		if(Fav1.contains(Manager1))
		{
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Manager is added to Favorite viewlet");
			System.out.println("Manager is added to Favorite viewlet");
		}
		else
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to add manager to favourite viewlet");
			System.out.println("Manager is not added to Favorite viewlet");
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=15)
	@TestRail(testCaseId = 61)
	public static void CompareManagers(ITestContext context) throws InterruptedException
	{
		//Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(2000);
		
		//Store the heading into string
		String CompareManagers=driver.findElement(By.xpath("//div[2]/div/div/span")).getText();
		//System.out.println(CompareManagers);
		
		//Verification of string
		if(CompareManagers.equalsIgnoreCase("Compare"))
		{
			System.out.println("Managers Compare page is opened");
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Managers Compare page is opened");
		}
		else
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to open comparision popup");
			System.out.println("Managers Compare page is opened");
			driver.findElement(By.xpath("Comparision failed")).click();
		}
		Thread.sleep(1000);
		
		//Differences only
		driver.findElement(By.cssSelector("div.differences > label.switch > span.slider.round")).click();
		
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
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Got an exception while comparing managers, check details: " + e.getMessage());
			System.out.println("No differences between Processes");
	       } 
		 
		//Closing the compare popup page
		driver.findElement(By.cssSelector(".close-button")).click();
	    Thread.sleep(1000);
	}
	
	
	@Test(priority=16)
	@TestRail(testCaseId = 62)
	public void StartAllWMQObjectsForMultipleManagers(ITestContext context) throws InterruptedException
	{
		try {
		//Select Start all wmq objects from commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li")).click();
		Thread.sleep(1000);
		
		//Click on Yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
		 context.setAttribute("Status", 1);
		 context.setAttribute("Comment", "WMQ objects are started using commands");
		}catch(Exception e)
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to start WMQ objects, check details: " + e.getMessage());
		}
		
	}
	
	@Test(priority=17)
	@TestRail(testCaseId = 63)
	public void StopAllWMQObjectsForMultipleManagers(ITestContext context) throws InterruptedException
	{
		try {
		//Select Stop all wmq objects from commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li[2]")).click();
		Thread.sleep(1000);
		
		//Click on Yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
		 context.setAttribute("Status", 1);
		 context.setAttribute("Comment", "WMQ objects are stopped using commands");
		}
		catch(Exception e)
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to stop WMQ objects, check details: " + e.getMessage());
		}
	}
	
	@Parameters({"MultipleDescription"})
	@TestRail(testCaseId = 64)
	@Test(priority=18)
	public void MultipleManagersProperties(String MultipleDescription, ITestContext context) throws InterruptedException
	{
		try {
		//Select properties option   
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(2000);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		
		//Description
		driver.findElement(By.id("qmngrDescription")).clear();
		driver.findElement(By.id("qmngrDescription")).sendKeys(MultipleDescription);
		Thread.sleep(1000);
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		//Select the properties option for First manager
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		
		//Store the First queue manager description into string
		String FirstQM=driver.findElement(By.id("qmngrDescription")).getAttribute("value");
		System.out.println("First Queue description is: " +FirstQM);
		Thread.sleep(1000);
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		//Select the properties option for Second manager
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		
		//Store the Second queue manager description into string
		String SecondQM=driver.findElement(By.id("qmngrDescription")).getAttribute("value");
		System.out.println("Second Queue description is: " +SecondQM);
		Thread.sleep(1000);
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		//Verification
		if(MultipleDescription.equals(FirstQM) && MultipleDescription.equals(SecondQM))
		{
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Multiple properties are fine for QM");
			System.out.println("Multiple properties are fine for QM");
		}
		else
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Multiple properties are not working for QM");
			System.out.println("Multiple properties are not working for QM");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(2000);
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while checking multiple properties for QM, check details: " + e.getMessage());
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		
	}
	
	@Test(priority=19)
	@TestRail(testCaseId = 65)
	public static void AddToFavoriteForMultipleManagers(ITestContext context) throws InterruptedException
	{
		int ManagerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ManagerName_Index=4;
		}
		
		//Manager names data storage                  
		String Manager2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
		System.out.println(Manager2);
		
		//Store the Manager name into string  
		String Manager3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
	
		try
		{
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByIndex(0);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".g-block-bottom-buttons > .g-button-blue")).click();
		Thread.sleep(4000);
		}
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Got exception while adding Multiple Managers to Favorite viewlet, check details: " + e.getMessage());
			System.out.println("Error ocuured");
			driver.findElement(By.cssSelector(".g-button-red")).click();
		}
		//Favorite viewlet data storing
		String Fav1=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		Thread.sleep(2000);
		
		//Verification of managers are present int favorite viewlet
		if(Fav1.contains(Manager2) && Fav1.contains(Manager3))
		{
			context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Multiple Managers are added to Favorite viewlet");
			System.out.println("Multiple Managers are added to Favorite viewlet");
		}
		else
		{
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to add Multiple Managers to Favorite viewlet");
			System.out.println("Multiple Managers are not added to Favorite viewlet");
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"QueueManagerNameFromOptions", "DefaultTransmissionQueueFromOptions", "DescriptionFromOptions"})
	
	@Test(priority=20)
	@TestRail(testCaseId = 66)
	public void CreateQueueManagerFromOptions(String QueueManagerNameFromOptions, String DefaultTransmissionQueueFromOptions, String DescriptionFromOptions,ITestContext context) throws InterruptedException
	{
		//Select the Create Queue manager option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Queue Manager")).click();
		Thread.sleep(1000);
		
		//Queue Details
		driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(QueueManagerNameFromOptions);
		driver.findElement(By.xpath("//app-qmgrcreatestep1/div/div[4]/div/input")).sendKeys(DefaultTransmissionQueueFromOptions);
		driver.findElement(By.xpath("//textarea")).sendKeys(DescriptionFromOptions);
		
		//Next button 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys("New Manager"); 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//Log Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Desktop");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//Data Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Test data path");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		
		//Final Submit
		driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/button")).click();
		Thread.sleep(20000);        
		
		try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e) 
		{
			System.out.println("No message is displaying");
		} 
		
		//Store the Manager viewlet data into string
		String ManagerData=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Verification condition 
		if(ManagerData.contains(QueueManagerNameFromOptions))
		{
			context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Queue Manager added successfully");
			System.out.println("Queue Manager is successfully added");
		}
		else
		{
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to add Queue manager");
			System.out.println("Queue Manager is not added");
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"SearchInputData"})
	@TestRail(testCaseId = 67)
	@Test(priority=21)
	public static void SearchFilter(String SearchInputData,ITestContext context) throws InterruptedException
	{
		//Click on search field
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(SearchInputData);
		Thread.sleep(2000);
		
		//Store the viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println(Viewletdata);
		       
		//Verify the Search data is present in the Viewlet
	    if(Viewletdata.toUpperCase().contains(SearchInputData.toUpperCase()))
	    {
	       System.out.println("Search is working fine");
	       context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Search option is working fine");
	    }
	    else
	    {
	       System.out.println("Search is not working fine");
	       context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Search option not working properly");
	       driver.findElement(By.xpath("Search is failed")).click();
	    }
	    
	    //Clear the search field data
	    for(int k=0; k<=SearchInputData.length(); k++)
	    {
	    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(2000);
	   
	}
	
	
	@Test(priority=30)
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
