package ApodGUI;

import java.io.File;
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
public class ListenerViewlet  
{
	String FinalListenerName="";
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String Dnode;
	static String DestinationManager;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		WGSName =Settings.getWGSNAME();
		UploadFilepath =Settings.getUploadFilepath();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
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
	
	@Parameters({"Listenerviewletname"})
	@TestRail(testCaseId=148)
	@Test(priority=1)
	public static void AddListenerViewlet(String Listenerviewletname, ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
				
		//Create Listener
		driver.findElement(By.cssSelector(".object-type:nth-child(7)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Listenerviewletname);	
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(WGSName);
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		if(driver.getPageSource().contains(Listenerviewletname))
		{
			System.out.println("Listener Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener viewlet is created successfully");
		}
		else
		{
			System.out.println("Listner viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create Listener viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
	    }
	
	@Parameters({"schemaName"})
	@TestRail(testCaseId=149)
	@Test(priority=22)
	public static void ShowObjectAttributesForListener(String schemaName, ITestContext context) throws InterruptedException
	{
		/*//click on checkbox to show attributes
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			

		String  AttributeName=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		//System.out.println(AttributeName);

		//Verification of Object Attribute page
		if(AttributeName.equals("Attributes"))
		{
		System.out.println("Show Object Attribute page is opened");
		}
		else
		{
		System.out.println("Show Object Attribute page is not opened");
		driver.findElement(By.xpath("Attribute page verification failed")).click();
		}
			
		//Close the object Attribute page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);*/
		try {
			
		
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ObjectAttributesVerification(driver, schemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for listeners are working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to show object attributes for listeners, check details: "+ e.getMessage());
		}
	}
	
	@Parameters({"ListenerName", "Description"})
	@TestRail(testCaseId=150)
	@Test(priority=3)
	public void CreateListener(String ListenerName, String Description, ITestContext context) throws InterruptedException
	{
		//click on checkbox and choose create listener
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Listener")).click();
		Thread.sleep(1000);
		
		//Create page 
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ListenerName);
		
		//Description
		driver.findElement(By.id("description")).sendKeys(Description);
		
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not dispalyed");
		}
		
		//Search with the added Listername name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);
		
		//Store the viewlet data into string
		String Listenerdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification condition
		if(Listenerdata.contains(ListenerName))
		{
			System.out.println("Listener is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is created successfully");
		}
		else
		{
			System.out.println("Listener is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create listeners");
			driver.findElement(By.xpath("Listener viewlet Failed")).click();
		}
		Thread.sleep(1000);
		
		//Edit the search field data
    	for(int j=0; j<=ListenerName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=151)
	@Test(priority=4)
	public void StartListener(String ListenerName, ITestContext context) throws InterruptedException
	{
		/*//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Start From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li")).click();
    	Thread.sleep(2000);
    	
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	//Store the Listener status into string
    	String Status=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status);
    	
    	if(Status.equalsIgnoreCase("Running") || Status.equalsIgnoreCase("Starting"))
    	{
    		System.out.println("Listener is Running");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is started using start command");
    	}
    	else
    	{
    		System.out.println("Listener is not Running");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to start listener using start command");
    		driver.findElement(By.xpath("Running failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=152)
	@Test(priority=5)
	public void StopListener(String ListenerName, ITestContext context) throws InterruptedException
	{
		/*//Search with the added process name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Stop From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[2]")).click();
    	
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	//Store the Listener status into string
    	String Status=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status);
    	
    	if(Status.equalsIgnoreCase("Stopping") || Status.equalsIgnoreCase("Stopped"))
    	{
    		System.out.println("Listener is Stoped");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is stopped using stop command");
    	}
    	else
    	{
    		System.out.println("Listener is not Stopped");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to stop listener using stop command");
    		driver.findElement(By.xpath("Stopping failed")).click();
    	}
    	Thread.sleep(1000);
    	
		
	}
	
	@Parameters({"CopyObjectName", "ListenerName"})
	@TestRail(testCaseId=153)
	@Test(priority=6)
	public void CopyAsFromCommands(String CopyObjectName, String ListenerName, ITestContext context) throws InterruptedException
	{
		//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);
		
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	
    	//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectName);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(5000);
    	
    	//Edit the search field data
    	for(int j=0; j<=ListenerName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	}
    	
    	//Combining the strings 
    	String CopyasListenerName=ListenerName+CopyObjectName;
    	System.out.println(CopyasListenerName);
    	    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasListenerName))
    	{
    		System.out.println("Listener is copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is copied using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Listener is not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy listener using CopyAs command");
    		driver.findElement(By.xpath("Listener failed to copy")).click();
    	}
    	Thread.sleep(1000);	
	}
	
	@Parameters({"RenameListener", "CopyObjectName", "ListenerName"})
	@TestRail(testCaseId=154)
	@Test(priority=7)
	public void RenameFromCommands(String RenameListener, String CopyObjectName, String ListenerName, ITestContext context) throws InterruptedException
	{ 
    	//Combining the strings 
    	String CopyasListenerName=ListenerName+CopyObjectName;
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasListenerName);
    	Thread.sleep(1000);
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(RenameListener);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	
    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasListenerName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	}
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListener);
    	Thread.sleep(1000);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int k=0; k<=RenameListener.length(); k++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameListener))
    	{
    		System.out.println("The Listener is renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is renamed using rename command");
    	}
    	else
    	{
    		System.out.println("The Listener rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename listener using rename command");
    		driver.findElement(By.xpath("Rename for Listener is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"RenameListener"})
	@TestRail(testCaseId=155)
	@Test(priority=8)
	public void DeleteFromCommands(String RenameListener, ITestContext context) throws InterruptedException
	{
		//Search with the deleted listener name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListener);
    	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	//Search with the new name
		for(int j=0; j<=RenameListener.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameListener))
    	{
    		System.out.println("Listener is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete listener using delete command");
    		driver.findElement(By.xpath("Listener delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Listener is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is deleted using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@Test(priority=9)
	@TestRail(testCaseId=156)
	public void ListenerProperties(ITestContext context) throws InterruptedException
	{
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(1000);
		
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Listener name field is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Listener name field is Disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
		}
		else
		{
			System.out.println("The Listener name field is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "The Listener name field is Enabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Listener field is disabled")).click();
			
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=10)
	@TestRail(testCaseId=157)
	public static void ListenerEvents(ITestContext context) throws InterruptedException
	{
		//click on checkbox and choose Events
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Events...")).click();
		Thread.sleep(1000);
			
		//Events Popup page
		String Eventdetails=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		//System.out.println(Eventdetails);
							
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
		System.out.println("Events page is opened");
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "The Listener events option is working fine");
		}
		else
		{
		System.out.println("Events page is not opened");
		context.setAttribute("Status",5);
		context.setAttribute("Comment", "The Listener events option is nt working properly");
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
	@TestRail(testCaseId=158)
	@Test(priority=11)
	public static void AddToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int ListenerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ListenerName_Index=4;
		}
		
		//Store Listener name into string
		String ListenerName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ ListenerName_Index +"]/div/span")).getText();
		
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
		
		//click on checkbox and choose to Add to favorite option
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
		
		//Verification of listener added to favorite viewlet
		if(Favdata.contains(ListenerName))
		{
			System.out.println("Listener name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Listener name is added to Favorite viewlet");
		}
		else
		{
			System.out.println("Listener name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Listener name to Favorite viewlet");
		
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
		
	@Test(priority=12)
	@TestRail(testCaseId=159)
	public static void CompareListeners(ITestContext context) throws InterruptedException
	{
		//Select Two checkboxes and choose compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Compare")).click();
			
		//Verification of popup
		String CompareProcess=driver.findElement(By.cssSelector("span.name")).getText();
		System.out.println(CompareProcess);
					
		if(CompareProcess.equalsIgnoreCase("Compare"))
		{
		System.out.println("Process Compare page is opened");
		}
		else
		{
			
		System.out.println("Process Compare page is not opened");
		context.setAttribute("Status",5);
		context.setAttribute("Comment", "Compare popup is not working properly");
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
         System.out.println("No differences between Listeners");
         context.setAttribute("Status",5);
 		context.setAttribute("Comment", "Got an exception while comparing listeners, check details: " + e.getMessage());
        }
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"ListenerNameFromICon", "DescriptionFromIcon"})
	@TestRail(testCaseId=167)
	@Test(priority=13)
	public void CreateListenerFromPlusIcon(String ListenerNameFromICon, String DescriptionFromIcon, ITestContext context) throws InterruptedException
	{
		//Click on + icon present in the viewlet
		driver.findElement(By.xpath("//img[@title='Add Listener']")).click();
		
		//Select WGS
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(WGSName);
		
		//Select Node 
		driver.findElement(By.xpath("//div[2]/input")).click();
		try 
		{
			List<WebElement> QueueNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(QueueNode.size());	
			for (int i=0; i<QueueNode.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + QueueNode.get(i).getAttribute("id"));
				String s=QueueNode.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=QueueNode.get(i).getAttribute("id");
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
		
		//Select Manager
		driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
		//driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		//Select Manager
       // driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
        try 
		{
			List<WebElement> QueueManager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(QueueManager.size());	
			for (int i=0; i<QueueManager.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + QueueManager.get(i).getAttribute("id"));
				String s=QueueManager.get(i).getText();
				if(s.equals(DestinationManager))
				{
					String id=QueueManager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
        Thread.sleep(1000);
		
		/*//Select Node 
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		
		//Select Manager
         driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
	     //driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
         driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div[2]")).click();*/
		
		//Click on Select path button
		driver.findElement(By.xpath("//div[3]/div/div/div/button")).click();
		Thread.sleep(1000);
		
		//Create page
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ListenerNameFromICon);
		
		//Description
		driver.findElement(By.id("description")).sendKeys(DescriptionFromIcon);
		Thread.sleep(2000);
		
		//Click on OK button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(4000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Click on Refresh
		driver.findElement(By.xpath("//div[3]/app-viewlet/div/div[2]/div/div[2]/div/div/img")).click();
		Thread.sleep(2000);
		
		//Search option
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerNameFromICon);
		Thread.sleep(1000);
		
		//Store the viewlet data into string
		String Listenerdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification condition
		if(Listenerdata.contains(ListenerNameFromICon))
		{
			System.out.println("Listener is created successfully from plus ICon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is created successfully using add Icon");
		}
		else
		{
			System.out.println("Listener is not created from Plus Icon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Failed to create Listener using add Icon");
			driver.findElement(By.xpath("Listener viewlet Failed")).click();
		}
		Thread.sleep(1000);
		
		//Edit the search field data
    	for(int j=0; j<=ListenerNameFromICon.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=160)
	@Test(priority=14)
	public void StartListenerForMultiple(String ListenerName, ITestContext context) throws InterruptedException
	{
	/*	//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Start From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li")).click();
    	Thread.sleep(2000);
    	
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	//Store the Listener status into string
    	String Status1=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status1);
    	String Status2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[9]/div/span")).getText();
    	System.out.println(Status2);
    	
    	if(Status1.equalsIgnoreCase("Running") || Status1.equalsIgnoreCase("Starting") && Status2.equalsIgnoreCase("Running") || Status2.equalsIgnoreCase("Starting"))
    	{
    		System.out.println("Multiple Listeners are Running");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are started using start command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners are not Running");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to start Multiple listeners using start command");
    		driver.findElement(By.xpath("Running failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=161)
	@Test(priority=15)
	public void StopListenerForMultiple(String ListenerName, ITestContext context) throws InterruptedException
	{
		/*//Search with the added process name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Stop From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li[2]")).click();
    	Thread.sleep(2000);
    	
    	try {
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	
    	
    	//Store the Listener status into string
    	String Status1=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status1);
    	String Status2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[9]/div/span")).getText();
    	System.out.println(Status2);
    	
    	if(Status1.equalsIgnoreCase("Stopping") || Status1.equalsIgnoreCase("Stopped") && Status1.equalsIgnoreCase("Stopping") || Status1.equalsIgnoreCase("Stopped"))
    	{
    		System.out.println("Multiple Listeners are Stoped");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are stpped using stop command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners are copyied not Stopped");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to stop Multiple listeners using stop command");
    		driver.findElement(By.xpath("Stopping failed")).click();
    	}
    	Thread.sleep(1000);
    	}
    	catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
		}
	}
	
	
	@Parameters({"CopyObjectNameForMUltiple", "ListenerName"})
	@TestRail(testCaseId=162)
	@Test(priority=16)
	public void CopyAsFromCommandsForMultiple(String CopyObjectNameForMUltiple, String ListenerName, ITestContext context) throws InterruptedException
	{
		//Select Copy as From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(2000);
    	
    	//Get the existing name
    	String ExistingListener=driver.findElement(By.xpath("//div[2]/div/input")).getText();
    	System.out.println("Existing listener name is: " +ExistingListener);
    	
    	//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectNameForMUltiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	
    	FinalListenerName=ExistingListener+CopyObjectNameForMUltiple;
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalListenerName);
    	Thread.sleep(1000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinalListenerName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinalListenerName))
    	{
    		System.out.println("Multiple Listeners are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are copied using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy Multiple listeners using CopyAs command");
    		driver.findElement(By.xpath("Listener failed to copy")).click();
    	}
    	Thread.sleep(1000);	
	}
	
	@Parameters({"RenameListenerForMultiple", "CopyObjectNameForMUltiple"})
	@TestRail(testCaseId=163)
	@Test(priority=17)
	public void RenameFromCommandsForMultiple(String RenameListenerForMultiple, String CopyObjectNameForMUltiple, ITestContext context) throws InterruptedException
	{
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalListenerName);
    	Thread.sleep(1000);
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(2000);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(RenameListenerForMultiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	
    	for(int k=0; k<=FinalListenerName.length(); k++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListenerForMultiple);
    	Thread.sleep(1000);  	
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<=RenameListenerForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameListenerForMultiple))
    	{
    		System.out.println("Multiple Listeners ares renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are renamed using rename command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename Multiple listeners using rename command");
    		driver.findElement(By.xpath("Rename for Listener is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"RenameListenerForMultiple"})
	@TestRail(testCaseId=164)
	@Test(priority=18)
	public void DeleteFromCommandsForMultiple(String RenameListenerForMultiple, ITestContext context) throws InterruptedException
	{
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListenerForMultiple);
    	Thread.sleep(1000);
    	
		//Select Delete From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(2000);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=RenameListenerForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameListenerForMultiple))
    	{
    		System.out.println("Multiple Listener are not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete Multiple listeners using delete command");
    		driver.findElement(By.xpath("Multiple Listeners are failed")).click();
    	}
    	else
    	{
    		System.out.println("Listener is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are deleted using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"ListenerDescription"})
	@TestRail(testCaseId=165)
	@Test(priority=19)
	public void ListenerMultipleProperties(String ListenerDescription, ITestContext context) throws InterruptedException
	{
		//Select Two Listeners and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(1000);
		
		//give the description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(ListenerDescription);
		Thread.sleep(2000);
		try {
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Open the first listener properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(1000);
		
		//Store the First listener description into string
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Open the second listener name
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(1000);
		
		//Store the second listener description into string
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Verification
		if(FirstDescription.equals(ListenerDescription) && SecondDescription.equals(ListenerDescription))
		{
			System.out.println("Multiple listener properties verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listener properties are verified successfully");
		}
		else
		{
			System.out.println("Multiple listener properties not verified");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to verify Multiple listener properties");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(1000);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
		}
		
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=166)
	@Test(priority=20)
	public static void AddToFavoriteForMultipleListeners(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int ListenerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ListenerName_Index=4;
		}
		
		//Store the Listeners into strings 
		String Listener2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ ListenerName_Index +"]/div/span")).getText();
		String Listener3=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ ListenerName_Index +"]/div/span")).getText();
		
		//Select Two Listeners and choose Add to favorite viewlet option
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
		
		//Verification of listeners added to favorite to favorite viewlet
		if(Favdata.contains(Listener2) && Favdata.contains(Listener3))
		{
			System.out.println("Multiple Listener names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listener properties added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Listener names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Multiple listener properties to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Test(priority=25)
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

