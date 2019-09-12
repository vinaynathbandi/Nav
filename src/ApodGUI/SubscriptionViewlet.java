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
public class SubscriptionViewlet {
	
	String FinalSubscription="";
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String DWGS;
	static String Dnode;
	static String DestinationManager;
	static String DestinationQueue;
	static String DWGSIcon;
	static String DestinationTopicName;
	static String Manager1;
	static String Manager1Queuename;
	static String Manager2Queuename;
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
		DWGS =Settings.getDWGS();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
		DestinationQueue =Settings.getDestinationQueue();
		DWGSIcon =Settings.getDWGSIcon();
		DestinationTopicName =Settings.getDestinationTopicName();
		Manager1 =Settings.getManager1();
		Manager1Queuename =Settings.getManager1Queuename();
		Manager2 =Settings.getManager2();
		Manager2Queuename =Settings.getManager2Queuename();
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
	@TestRail(testCaseId=186)
    @Parameters({"Subscriptionname"})
	public static void AddSubscriptionViewlet(String Subscriptionname, ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		//Create subscription
		driver.findElement(By.cssSelector(".object-type:nth-child(12)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Subscriptionname);
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(WGSName);
		
	    //Click on Save changes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Subscription viewlet verification condition
		if(driver.getPageSource().contains(Subscriptionname))
		{
			System.out.println("Subscription Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription viewlet is created successfully");
		}
		else
		{
			System.out.println("Subscription viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create subscription viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
			
    }
	
	@Parameters({"SubscriptionAttributes", "schemaName", "AddSubscriptionNameFromIcon"})
	@TestRail(testCaseId=188)
	@Test(priority=18)
	public static void ShowObjectAttributesForSubscription(String SubscriptionAttributes, String schemaName, String AddSubscriptionNameFromIcon, ITestContext context) throws InterruptedException
	{
		/*//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);String  AttributeName=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		System.out.println(AttributeName);
		Thread.sleep(1000);
		
		//Verification of Object Attribute page
		if(AttributeName.equals(SubscriptionAttributes))
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
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionNameFromIcon);
		Thread.sleep(2000);
		
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.SubscriptionObjectAttributesVerification(driver, schemaName, AddSubscriptionNameFromIcon, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for subscription is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to show object attributes for subscription viewlet");
		}
	}
	
	@Parameters({"AddSubscriptionName", "TopicStringData"})
	@TestRail(testCaseId=187)
	@Test(priority=2)
	public void CreateSubscriptionFromOptions(String AddSubscriptionName, String TopicStringData, ITestContext context) throws InterruptedException
	{
		//Search with given Queue manager
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DestinationManager);
		
		//click on checkbox and choose create subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Subscription")).click();
		Thread.sleep(1000);
		
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionName);
		
		//Select the Topic name from the list
		try 
		{
			driver.findElement(By.id("topicName")).click();
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(DestinationTopicName))
				{
					String id=Topic.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(4000);
		
		//Topic string data
		driver.findElement(By.id("topicString")).sendKeys(TopicStringData);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		
		//Select WGS name
		Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
		DestinationWGS.selectByVisibleText(DWGS);
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			driver.findElement(By.id("destinationNodeName")).click();
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
				//System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
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
		
		//Select Manager value
		try 
		{
			driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(s.equals(DestinationManager))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		//Select Queue name
		try 
		{
			driver.findElement(By.id("destinationQName")).click();
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				//System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				//System.out.println(s);
				if(s.equals(DestinationQueue))
				{
					String id=QueueName.get(i).getAttribute("id");
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
				
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error message not displayed");
    	}
    	Thread.sleep(2000);
    	
    	for(int i=0; i<=DestinationManager.length(); i++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionName);
    	Thread.sleep(1000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println("Subscription body data: " +Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=AddSubscriptionName.length(); j++)
    	{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification
    	if(Subviewlet.contains(AddSubscriptionName))
    	{
    		System.out.println("Subscription is added successfully");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription added successfully");
    	}
    	else
    	{
    		System.out.println("Subscription is not added to the viewlet");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add subscription");
    		driver.findElement(By.xpath("Subscription failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"CopyObjectName", "AddSubscriptionName"})
	@TestRail(testCaseId=189)
	@Test(priority=3)
	public void CopyAsFromCommands(String CopyObjectName, String AddSubscriptionName, ITestContext context) throws InterruptedException
	{
		//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionName);
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
    	for(int j=0; j<=AddSubscriptionName.length(); j++)
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
    	String CopyasSubscriptionName=AddSubscriptionName+CopyObjectName;
    	System.out.println(CopyasSubscriptionName);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasSubscriptionName))
    	{
    		System.out.println("Subscription is copied");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription is copied successfully using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Subscription is not copied");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to copy subscription using CopyAs command");
    		
    		//Search with that name
        	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasSubscriptionName);
        	Thread.sleep(2000);
    		driver.findElement(By.xpath("Subscription failed to copy")).click();
    	}
    	Thread.sleep(1000);	
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasSubscriptionName);
    	Thread.sleep(2000);
	}
	
	@Parameters({"RenameSubscription", "CopyObjectName", "AddSubscriptionName"})
	@TestRail(testCaseId=190)
	@Test(priority=4)
	public void RenameFromCommands(String RenameSubscription, String CopyObjectName, String AddSubscriptionName, ITestContext context) throws InterruptedException
	{    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(RenameSubscription);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(5000);
    	
    	//Combining the strings 
    	String CopyasSubscriptionName=AddSubscriptionName+CopyObjectName;
    	//System.out.println(CopyasProcessName);
    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasSubscriptionName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(1000);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscription);
    	Thread.sleep(1000); 
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameSubscription))
    	{
    		System.out.println("The Subscription is renamed");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Successfully renamed subscription rename command");
    	}
    	else
    	{
    		System.out.println("The Subscription rename is failed");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to rename subscription");
    		driver.findElement(By.xpath("Rename for subscription is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"RenameSubscription"})
	@TestRail(testCaseId=191)
	@Test(priority=5)
	public void DeleteFromCommands(String RenameSubscription,ITestContext context) throws InterruptedException
	{   	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=RenameSubscription.length(); j++)
    	{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameSubscription))
    	{
    		System.out.println("Subscription is not deleted");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription is deleted successfully using delete command");
    		driver.findElement(By.xpath("Subscription delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Subscription is deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete subscription using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	
	
	@Test(priority=6)
	@TestRail(testCaseId=192)
	public void SubscriptionProperties(ITestContext context) throws InterruptedException
	{
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(1000);
		
		try
		{
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Subscription name field is Disabled");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription option is working fine");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			/*if(driver.findElement(By.id("yes")).isDisplayed())
			{
				driver.findElement(By.id("yes")).click();
			}*/
		}
		else
		{
			System.out.println("The Subscription name field is Enabled");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Subscription option is not working properly");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Subscription field is disabled")).click();
			
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception Occured");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Exception occured while checking subscription properties, check details: "+ e.getMessage());
			driver.findElement(By.id("yes")).click();
		}
		Thread.sleep(2000);
		
	}
	
    @Test(priority=7)
    @TestRail(testCaseId=193)
    public static void SubscriptionEvents(ITestContext context) throws InterruptedException 
    {
    	//Select Events option
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Events...")).click();
    	Thread.sleep(1000);//Events Popup page
		String Eventdetails=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		//System.out.println(Eventdetails);
						
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription event option is working fine");
		}
		else
		{
			System.out.println("Events page is not opened");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to open subscription event option");
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
    @TestRail(testCaseId=194)
	@Test(priority=8)
	public static void AddToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
    	int Subscription_Id=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Subscription_Id=6;
    	}
    	//Store the subscription Name into string
		String SubscriptionId=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ Subscription_Id +"]/div/span")).getText();
		//System.out.println(SubscriptionId);
		
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
		
		//Add to favorite option
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
		//System.out.println(Favdata);
		
		//Verifiation of subscription added to favorite viewlet
		if(Favdata.contains(SubscriptionId))
		{
			System.out.println("Subscription is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription successfully added to Favorite viewlet");
		}
		else
		{
			System.out.println("Subscription is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add subscription to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);

	}
	
	
	@Test(priority=9)
	@TestRail(testCaseId=195)
	public static void CompareSubscription(ITestContext context) throws InterruptedException
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
			System.out.println("Subscription Compare page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Comparision of subscription is working fine");
		
		}
		else
		{
			System.out.println("Subscription Compare page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to compare subscription");
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
         System.out.println("No differences between Names lists");
         context.setAttribute("Status",5);
			context.setAttribute("Comment", "Exception occured while comparing subscriptions, check details: "+ e.getMessage());
        }
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);	
	}
	
	@Parameters({"DestinationNodeName", "DestinationManagerName", "AddSubscriptionNameFromIcon", "TopicStringDataFromICon", "NodeNameFromIcon", "DestinationManagerFromIcon", "DestinationQueueFromIcon"})
	@TestRail(testCaseId=201)
	@Test(priority=10)
	public void CreateSubscriptionFromPlusIcon(String DestinationNodeName, String DestinationManagerName, String AddSubscriptionNameFromIcon, String TopicStringDataFromICon, String NodeNameFromIcon, String DestinationManagerFromIcon, String DestinationQueueFromIcon,ITestContext context) throws InterruptedException
	{
		String[] Managers= {Manager1, Manager2};
		//System.out.println("Size of managers:" +Managers.length);
		for(int m=0; m<=1; m++)
		{
		//Click on + icon present in the listener viewlet
		driver.findElement(By.xpath("//img[@title='Add Subscription']")).click();
		
		//Select WGS
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(WGSName);
		
		//Select Node
		driver.findElement(By.xpath("//ng-select/div")).click();
		Thread.sleep(1000);
		
		try 
		{
			List<WebElement> TopicNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(TopicNode.size());	
			for (int i=0; i<TopicNode.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + TopicNode.get(i).getAttribute("id"));
				String s=TopicNode.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=TopicNode.get(i).getAttribute("id");
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
        Thread.sleep(2000);
        try 
		{
			List<WebElement> TopicManager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(TopicManager.size());	
			for (int i=0; i<TopicManager.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + TopicManager.get(i).getAttribute("id"));
				String s=TopicManager.get(i).getText();
				if(Managers[m].contains(s))
				{
					String id=TopicManager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
				else
				{
					System.out.println("Managers are not matched");
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
        Thread.sleep(1000);
        
		//Click on Select path button
		driver.findElement(By.xpath("//div[3]/div/div/div/button")).click();
		Thread.sleep(1000);
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionNameFromIcon);
		Thread.sleep(4000);
		
		//Select the Topic name from the list
		try 
		{
			driver.findElement(By.id("topicName")).click();
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(DestinationTopicName))
				{
					String id=Topic.get(i).getAttribute("id");
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
		
		//Topic string data
		driver.findElement(By.id("topicString")).sendKeys(TopicStringDataFromICon);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		
		//Select WGS name
		Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
		DestinationWGS.selectByVisibleText(DWGSIcon);
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			driver.findElement(By.id("destinationNodeName")).click();
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
				//System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
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
		
		//Select Manager value
		try 
		{
			driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(Managers[m].contains(s))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		//Select Queue name
		try 
		{
			driver.findElement(By.id("destinationQName")).click();
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				//System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				if(Managers[m].equalsIgnoreCase(Manager1))
				{
				if(s.equals(Manager1Queuename))
				{
					String id=QueueName.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
				}
				else
				{
					if(s.equals(Manager2Queuename))
					{
						String id=QueueName.get(i).getAttribute("id");
						driver.findElement(By.id(id)).click();
						break;
					}
				}
					
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
				
		//Click on OK button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
    	Thread.sleep(6000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error popup is occur");
    	}
    	
    	//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionNameFromIcon);
    	Thread.sleep(1000);
	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=AddSubscriptionNameFromIcon.length(); j++)
    	{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification
    	if(Subviewlet.contains(AddSubscriptionNameFromIcon))
    	{
    		System.out.println("Subscription is added successfully");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription is created successfully using add Icon");
    	}
    	else
    	{
    		System.out.println("Subscription is not added to the viewlet");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create subscription using add Icon");
    		driver.findElement(By.xpath("Subscription failed")).click();
    	}
    	Thread.sleep(4000);
	}
	}
	
	@Parameters({"CopyObjectNameForMUltiple", "AddSubscriptionNameFromIcon"})
	@TestRail(testCaseId=196)
	@Test(priority=11)
	public void CopyAsFromCommandsForMultipleSubscriptions(String CopyObjectNameForMUltiple, String AddSubscriptionNameFromIcon, ITestContext context) throws InterruptedException
	{
		//Search withe Subscription names
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionNameFromIcon);
    	
		//Select Copy As From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(2000);
    	
    	//Get the existing subscription name
    	String ExistingSubscription=driver.findElement(By.xpath("//div[2]/div/input")).getText();
    	System.out.println("Existing subscription name: " +ExistingSubscription);
    	
    	//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectNameForMUltiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	FinalSubscription=ExistingSubscription+CopyObjectNameForMUltiple;
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalSubscription);
    	Thread.sleep(1000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinalSubscription.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinalSubscription))
    	{
    		System.out.println("Multiple Subscriptions are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Copying multiple subscriptions is working fine");
    	}
    	else
    	{
    		System.out.println("Multiple Subscriptions are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy multiple subscriptions");
    		driver.findElement(By.xpath("Subscriptions failed to copy")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"RenameSubscriptionForMultiple"})
	@TestRail(testCaseId=197)
	@Test(priority=12)
	public void RenameFromCommandsForMultipleSubscriptions(String RenameSubscriptionForMultiple, ITestContext context) throws InterruptedException
	{
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalSubscription);
    	Thread.sleep(1000);
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(2000);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(RenameSubscriptionForMultiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	for(int j=0; j<=FinalSubscription.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscriptionForMultiple);
    	Thread.sleep(1000);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<=RenameSubscriptionForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameSubscriptionForMultiple))
    	{
    		System.out.println("Multiple Subscriptions ares renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Renaming multiple subscriptions is working fine");
    	}
    	else
    	{
    		System.out.println("Multiple Subscriptions rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename multiple subscriptions");
    		driver.findElement(By.xpath("Rename for Subscriptions is failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"RenameSubscriptionForMultiple"})
	@TestRail(testCaseId=198)
	@Test(priority=13)
	public void DeleteFromCommandsForMultipleSubscriptions(String RenameSubscriptionForMultiple, ITestContext context) throws InterruptedException
	{
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscriptionForMultiple);
    	Thread.sleep(1000);
    	
		//Select Delete From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(6000);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	/*//clear the search data
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	Thread.sleep(1000);*/
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=RenameSubscriptionForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameSubscriptionForMultiple))
    	{
    		System.out.println("Multiple Subscriptions is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete multiple subscriptions");
    		driver.findElement(By.xpath("Multiple Subscriptions are failed")).click();
    	}
    	else
    	{
    		System.out.println("Subscriptions is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deleting multiple subscriptions is working fine");
    	}
    	Thread.sleep(1000);
		
	}
	
	@Test(priority=14)
	@TestRail(testCaseId=199)
	public void MultipleSubscriptionProperties(ITestContext context) throws InterruptedException
	{
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(3000);
		
		WebElement ele=driver.findElement(By.id("topicString"));
		Actions a=new Actions(driver);
		a.moveToElement(ele).perform();
	
		//List<WebElement> data= (List<WebElement>) driver.findElement(By.tagName("app-text-input-tooltip")).findElements(By.xpath("//table//tr"));
		//String Tooltipdata=driver.findElement(By.tagName("app-text-input-tooltip")).getText();
		String Tooltipdata=driver.findElement(By.tagName("ngb-tooltip-window")).getText();
		System.out.println("Multiple Properties data:" +Tooltipdata);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//click on checkbox and choose properties of first subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(1000);
		
		String FistSubscription=driver.findElement(By.id("topicString")).getAttribute("value");
		
		FistSubscription=FistSubscription.trim().replaceAll(" +", " ");
		System.out.println("First subscription:" +FistSubscription);
		
		//Clsoe the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//click on checkbox and choose properties of first subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(1000);
		
		String SecondSubscription=driver.findElement(By.id("topicString")).getAttribute("value");
		
		SecondSubscription=SecondSubscription.trim().replaceAll(" +", " ");
		System.out.println("Second subscription:" +SecondSubscription);
		
		//Clsoe the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		if(Tooltipdata.contains(FistSubscription) && Tooltipdata.contains(SecondSubscription))
		{
			System.out.println("Subscription multiple properties verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple subscription properties are verified successfully");
		}
		else
		{
			System.out.println("Subscription multiple properties not verified");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Verification failed for multiple subscription properties");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(1000);
		
		/*try
		{
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Subscription name field is Disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			if(driver.findElement(By.id("yes")).isDisplayed())
			{
				driver.findElement(By.id("yes")).click();
			}
		}
		else
		{
			System.out.println("The Subscription name field is Enabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Subscription field is disabled")).click();
			
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception Occured");
			driver.findElement(By.id("yes")).click();
		}
		Thread.sleep(2000);*/
		
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=200)
	@Test(priority=15)
	public static void AddToFavoriteForMultipleSubscription(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int Subscription_Id=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Subscription_Id=6;
    	}
    	
		//Store the Subscription ids into string
		String SubscriptionId2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Subscription_Id +"]/div/span")).getText();
		//System.out.println(SubscriptionId2);
		String SubscriptionId3=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ Subscription_Id +"]/div/span")).getText();
		//System.out.println(SubscriptionId3);
		
		//Select compare option
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
		System.out.println(Favdata);
		
		//Verification of subscriptions added to favorite viewlet
		if(Favdata.contains(SubscriptionId2) && Favdata.contains(SubscriptionId3))
		{
			System.out.println("Multiple Subscriptions are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple subscription are added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Subscriptions are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add multiple subscription to favorite viewlet");
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
